package com.example.wmsspringbootproject.common.designmode;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.wmsspringbootproject.Service.SysUserTypeService;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.common.Annotation.Listener;
import com.example.wmsspringbootproject.common.Annotation.LogNote;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import com.example.wmsspringbootproject.im.websocket.*;
import com.example.wmsspringbootproject.im.websocket.constants.ImConfigConst;
import com.example.wmsspringbootproject.model.entity.SysUserType;
import com.example.wmsspringbootproject.model.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class TransactionNotify {

    @Autowired
    UserService userService;

    @Autowired
    SysUserTypeService sysUserTypeService;

    @Autowired
    MessageCache messageCache;

    @Listener(filedName = "create_transaction")
    @LogNote(description = "通知管理者")
    public void sendNotify(String warehouseId,String status){
        List<SysUserType> userTypeList=sysUserTypeService.lambdaQuery()
                .eq(SysUserType::getRoleId, Constants.RoleType.ROOT.getValue())
                .or(r->r.eq(SysUserType::getRoleId, Constants.RoleType.ADMIN.getValue())).list();
        LambdaQueryChainWrapper<Users> query =userService.lambdaQuery();
        query.or(rora->
                rora.in(Users::getId,
                        userTypeList.stream().map(SysUserType::getUserId).toList()
                )
        );
        List<Users> usersList=query
                .eq(Users::getWarehouseId,warehouseId).list();
        String content="叮，您有新的待办任务哦！";
        for (Users users : usersList) {
            ImChatUserMessage imChatUserMessage=new ImChatUserMessage();
            imChatUserMessage.setFromId(Convert.toInt(Constants.CONFIRM_TRANSACTION_NOTIFY_ID));

            if(Objects.equals(Convert.toInt(status),Constants.transactionStatus.CREATE_SUCCESS))
                imChatUserMessage.setFromId(Convert.toInt(Constants.CREATE_TRANSACTION_NOTIFY_ID));
            imChatUserMessage.setContent(content);
            imChatUserMessage.setCreateTime(LocalDateTime.now());
            imChatUserMessage.setToId(users.getId().toString());
            imChatUserMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_FALSE);
            messageCache.putUserMessage(imChatUserMessage);
            send(imChatUserMessage,users);
        }
    }

    @Listener(filedName = "update_transaction")
    @LogNote(description = "通知创建者")
    public void notifyCreator(String status,String creatorId){
        List<SysUserType> userTypeList=sysUserTypeService.lambdaQuery()
                .eq(SysUserType::getRoleId, Constants.RoleType.ROOT.getValue())
                .or(r->r.eq(SysUserType::getRoleId, Constants.RoleType.ADMIN.getValue())).list();

        LambdaQueryChainWrapper<Users> query =userService.lambdaQuery();
        if(Objects.equals(Convert.toInt(status),Constants.transactionStatus.CONFIRM)){
            query.and(rora->
                    rora.in(Users::getId,
                            userTypeList.stream().map(SysUserType::getUserId).toList()
                    )
            );
        }else{
            query.eq(Users::getId,Convert.toInt(creatorId));
        }
        List<Users> users=query.list();

        for (Users user : users) {
            ImChatUserMessage imChatUserMessage=new ImChatUserMessage();
            imChatUserMessage.setFromId(Convert.toInt(Constants.TRANSACTION_ISSUE_REPORT));
            String content="叮，您有新的异常任务";
            if(Objects.equals(Convert.toInt(status),Constants.transactionStatus.AUDIT_SUCCESS)){
                imChatUserMessage.setFromId(Convert.toInt(Constants.TRANSACTION_AUDIT_SUCCESS));
                content="叮，您有新的审核完成的任务";
            }else if(Objects.equals(Convert.toInt(status),Constants.transactionStatus.CONFIRM)){
                imChatUserMessage.setFromId(Convert.toInt(Constants.CONFIRM_TRANSACTION_NOTIFY_ID));
                content="叮，您有新的任务已经被确认！";
            }
            imChatUserMessage.setContent(content);
            imChatUserMessage.setCreateTime(LocalDateTime.now());
            imChatUserMessage.setToId(user.getId().toString());
            imChatUserMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_FALSE);
            messageCache.putUserMessage(imChatUserMessage);
            send(imChatUserMessage,user);
        }
    }

    public void send(ImChatUserMessage imChatUserMessage,Users users){
        ImMessage imMessage=new ImMessage();
        imMessage.setMessageType(ImEnum.MESSAGE_TYPE_MSG_SINGLE.getCode());
        imMessage.setContent(imChatUserMessage.getContent());
        imMessage.setAvatar("");
        imMessage.setFromId(imChatUserMessage.getFromId());
        imMessage.setToId(Convert.toInt(imChatUserMessage.getToId()));
        imMessage.setUsername("事务系统");

        TioWebsocketStarter tioWebsocketStarter=TioUtil.getTio();
        WsResponse wsResponse=WsResponse.fromText(JSON.toJSONString(imMessage),ImConfigConst.CHARSET);
        if(tioWebsocketStarter!=null){
            SetWithLock<ChannelContext> setWithLock = Tio.getByUserid(tioWebsocketStarter.getServerTioConfig(),users.getId().toString());
            if(setWithLock!=null&&setWithLock.size()>0){
                Tio.sendToUser(tioWebsocketStarter.getServerTioConfig(),imMessage.getToId().toString(),wsResponse);
            }
        }
    }

}
