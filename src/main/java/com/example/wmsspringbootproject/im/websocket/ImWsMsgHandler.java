package com.example.wmsspringbootproject.im.websocket;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.wmsspringbootproject.Utils.CommonQuery;
import com.example.wmsspringbootproject.Utils.SecurityUtils;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.Utils.WmsCache;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.core.security.model.SysUserDetails;
import com.example.wmsspringbootproject.im.http.entity.ImChatGroupUser;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserGroupMessage;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import com.example.wmsspringbootproject.im.http.service.ImChatGroupUserService;
import com.example.wmsspringbootproject.im.http.service.ImChatUserMessageService;
import com.example.wmsspringbootproject.im.websocket.constants.ImConfigConst;
import com.example.wmsspringbootproject.model.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 消息代理类
 * </p>
 *
 * @author 初秋
 * @since 2024-06-10
 */
@Component
@Slf4j
public class ImWsMsgHandler implements IWsMsgHandler {

    @Autowired
    private ImChatGroupUserService imChatGroupUserService;

    @Autowired
    private ImChatUserMessageService imChatUserMessageService;

    @Autowired
    private MessageCache messageCache;

    @Autowired
    private CommonQuery commonQuery;
    /**
     * 握手时走这个方法，业务可以在这里获取cookie，request等
     * 对httpResponse参数进行补充并返回，如果返回null表示不想和对方建立连接
     * 对于大部分业务，该方法只需要一行代码：return httpResponse;
     */
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) {
        String token = httpRequest.getParam(Constants.TOKEN_HEADER);

        if (!StringUtils.hasText(token)) {
            return null;
        }

        Users user = (Users) WmsCache.get(token);

        if (user == null) {
            return null;
        }

        log.info("握手成功：用户ID：{}, 用户名：{}", user.getId(), user.getName());

        return httpResponse;
    }

    /**
     * 握手成功后触发该方法
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) {
        String token = httpRequest.getParam(Constants.TOKEN_HEADER);
        // TODO 获取当前登录的用户
        SysUserDetails user = (SysUserDetails) WmsCache.get(token);
        Tio.closeUser(channelContext.tioConfig, String.valueOf(user.getId()), null);
        Tio.bindUser(channelContext, String.valueOf(user.getId()));

        //TODO 获取用户单聊未读消息 列表
        List<ImChatUserMessage> userMessages = imChatUserMessageService.lambdaQuery()
                .eq(ImChatUserMessage::getToId, user.getId())
                .eq(ImChatUserMessage::getMessageStatus, ImConfigConst.USER_MESSAGE_STATUS_FALSE)
                .or(ms->
                        ms.eq(ImChatUserMessage::getFromId,ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID))
                .orderByAsc(ImChatUserMessage::getCreateTime).list();

        //TODO 遍历消息列表 推送到目标用户
        if (!CollectionUtils.isEmpty(userMessages)) {
            List<Long> ids = new ArrayList<>();
            userMessages.forEach(userMessage -> {
                ids.add(userMessage.getId());
                ImMessage imMessage = new ImMessage();
                imMessage.setContent(userMessage.getContent());
                imMessage.setFromId(userMessage.getFromId());
                imMessage.setToId(Integer.parseInt(userMessage.getToId()));
                imMessage.setMessageType(ImEnum.MESSAGE_TYPE_MSG_SINGLE.getCode());
                Users friend = commonQuery.getUser(userMessage.getFromId());
                if (friend != null) {
                    imMessage.setAvatar(friend.getAvatar());
                }
                WsResponse wsResponse = WsResponse.fromText(JSON.toJSONString(imMessage), ImConfigConst.CHARSET);
                Tio.sendToUser(channelContext.tioConfig, String.valueOf(user.getId()), wsResponse);
            });
            //TODO  更新数据库中消息的状态为已读
            imChatUserMessageService.lambdaUpdate().in(ImChatUserMessage::getId, ids)
                    .set(ImChatUserMessage::getMessageStatus, ImConfigConst.USER_MESSAGE_STATUS_TRUE).update();

        }

        //TODO 找到用户所在的所有（群状态是：1、验证通过的  2、被禁言的）群
        LambdaQueryChainWrapper<ImChatGroupUser> lambdaQuery = imChatGroupUserService.lambdaQuery();
        lambdaQuery.select(ImChatGroupUser::getGroupId);
        lambdaQuery.eq(ImChatGroupUser::getUserId, user.getId());
        lambdaQuery.in(ImChatGroupUser::getUserStatus, ImConfigConst.GROUP_USER_STATUS_PASS, ImConfigConst.GROUP_USER_STATUS_SILENCE);
        List<ImChatGroupUser> groupUsers = lambdaQuery.list();
        //TODO 在频道上下文中创建 对应群
        if (!CollectionUtils.isEmpty(groupUsers)) {
            groupUsers.forEach(groupUser -> Tio.bindGroup(channelContext, groupUser.getGroupId().toString()));
        }
    }

    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) {
        return null;
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) {
        Tio.remove(channelContext, "连接关闭");
        return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) {
        //消息为空则直接返回null
        if (!StringUtils.hasText(text)) {
            return null;
        }
        try {
            ImMessage imMessage = JSON.parseObject(text, ImMessage.class);

            String content = TextUtil.removeHtml(imMessage.getContent());
            if (!StringUtils.hasText(content)) {
                return null;
            }
            imMessage.setContent(content);

            WsResponse wsResponse = WsResponse.fromText(JSON.toJSONString(imMessage), ImConfigConst.CHARSET);
            if (imMessage.getMessageType().intValue() == ImEnum.MESSAGE_TYPE_MSG_SINGLE.getCode()) {
                //单聊
                ImChatUserMessage userMessage = new ImChatUserMessage();
                userMessage.setFromId(imMessage.getFromId());
                userMessage.setToId(String.valueOf(imMessage.getToId()));
                userMessage.setContent(imMessage.getContent());
                userMessage.setCreateTime(LocalDateTime.now());

                //TODO 判断接受用户是否在线
                SetWithLock<ChannelContext> setWithLock = Tio.getByUserid(channelContext.tioConfig, imMessage.getToId().toString());
                if (setWithLock != null && setWithLock.size() > 0) {
                    // 在线 将消息发送给对应用户 更新消息状态
                    Tio.sendToUser(channelContext.tioConfig, imMessage.getToId().toString(), wsResponse);
                    userMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_TRUE);
                } else {
                    //未在线 记录消息状态为 未读
                    userMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_FALSE);
                }
                //将消息存入缓存中
                messageCache.putUserMessage(userMessage);
                //将消息发送给 发送方
                Tio.sendToUser(channelContext.tioConfig, imMessage.getFromId().toString(), wsResponse);
            } else if (imMessage.getMessageType().intValue() == ImEnum.MESSAGE_TYPE_MSG_GROUP.getCode()) {
                //群聊
                ImChatUserGroupMessage groupMessage = new ImChatUserGroupMessage();
                groupMessage.setContent(imMessage.getContent());
                groupMessage.setFromId(imMessage.getFromId());
                groupMessage.setGroupId(imMessage.getGroupId());
                groupMessage.setCreateTime(LocalDateTime.now());
                groupMessage.setToId(imMessage.getGroupId().toString());

                //TODO 判断群中是否有成员在线
                SetWithLock<ChannelContext> setWithLock = Tio.getByGroup(channelContext.tioConfig, imMessage.getGroupId().toString());
                if (setWithLock != null && setWithLock.size() > 0) {
                    groupMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_FALSE);
                    messageCache.putGroupMessage(groupMessage);
                    Tio.sendToGroup(channelContext.tioConfig, imMessage.getGroupId().toString(), wsResponse);
                }
            }
        } catch (Exception e) {
            log.error("解析消息失败：{}", e.getMessage());
        }
        //返回值是要发送给客户端的内容，一般都是返回null
        return null;
    }
}
