package com.example.wmsspringbootproject.Utils;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.example.wmsspringbootproject.im.http.entity.ImChatGroup;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserGroupMessage;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import com.example.wmsspringbootproject.im.websocket.ImEnum;
import com.example.wmsspringbootproject.im.websocket.ImMessage;
import com.example.wmsspringbootproject.im.websocket.TioUtil;
import com.example.wmsspringbootproject.im.websocket.TioWebsocketStarter;
import com.example.wmsspringbootproject.im.websocket.constants.ImConfigConst;
import org.tio.core.Tio;
import org.tio.websocket.common.WsResponse;

import java.time.LocalDateTime;

/**
 * @apiNote 推送系统通知消息
 * @author 初秋
 * @version 1.0
 * @since 2024-06-12
 */
public class SysPushMessage {

    /**
     * @apiNote 通知被操作 用户
     * @param toId 接收方id
     * @param imChatUserGroupMessage 消息实例
     * @param groupId 申请的群id
     */
    public static void send(String toId, ImChatUserGroupMessage imChatUserGroupMessage,Integer groupId){
        ImMessage imMessage=new ImMessage();
        //FIXME 后续可将头像 替换成系统的头像 地址
        imMessage.setUsername("StellarWMS系统提醒");
        imMessage.setAvatar("");
        imMessage.setContent(imChatUserGroupMessage.getContent());
        imMessage.setGroupId(groupId);
        imMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        imMessage.setToId(Integer.parseInt(toId));
        imMessage.setMessageType(ImEnum.MESSAGE_TYPE_MSG_SINGLE.getCode());

        TioWebsocketStarter tioWebsocketStarter= TioUtil.getTio();
        if(tioWebsocketStarter!=null){
            WsResponse wsResponse=WsResponse.fromText(JSON.toJSONString(imMessage),ImConfigConst.CHARSET);
            Tio.sendToUser(tioWebsocketStarter.getServerTioConfig(),imMessage.getToId().toString(),wsResponse);
        }
    }

    public static void send(ImChatUserMessage imChatUserMessage){
        ImMessage imMessage=new ImMessage();
        imMessage.setUsername("StellarWMS系统提醒");
        imMessage.setAvatar("");
        imMessage.setContent(imChatUserMessage.getContent());
        imMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        imMessage.setToId(imMessage.getToId());
        imMessage.setMessageType(ImEnum.MESSAGE_TYPE_MSG_GROUP.getCode());

        TioWebsocketStarter tioWebsocketStarter= TioUtil.getTio();
        if(tioWebsocketStarter!=null){
            WsResponse wsResponse=WsResponse.fromText(JSON.toJSONString(imMessage),ImConfigConst.CHARSET);
            Tio.sendToUser(tioWebsocketStarter.getServerTioConfig(),imMessage.getToId().toString(),wsResponse);
        }
    }

    /**
     * @apiNote 通知相关群
     * @param message 消息实例
     */
    public static void send(ImChatUserGroupMessage message){
        ImMessage imMessage=new ImMessage();
        imMessage.setContent(message.getContent());
        imMessage.setGroupId(message.getGroupId());
        imMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        imMessage.setMessageType(ImEnum.MESSAGE_TYPE_MSG_GROUP.getCode());

        TioWebsocketStarter tioWebsocketStarter= TioUtil.getTio();
        if(tioWebsocketStarter!=null){
            WsResponse wsResponse=WsResponse.fromText(JSON.toJSONString(imMessage),ImConfigConst.CHARSET);
            Tio.sendToGroup(tioWebsocketStarter.getServerTioConfig(),imMessage.getGroupId().toString(),wsResponse);
        }
    }

    /**
     * @apiNote 创建系统单聊通知消息实例
     * @param userId 被通知的用户id
     * @param content 消息内容
     * @return 单聊消息实例
     */
    public static ImChatUserMessage builderMessage(Integer userId,String content)
    {
        ImChatUserMessage imChatUserMessage=new ImChatUserMessage();
        imChatUserMessage.setContent(content);
        imChatUserMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        imChatUserMessage.setToId(Convert.toStr(userId));
        imChatUserMessage.setCreateTime(LocalDateTime.now());
        if(WmsCache.getTwainMap().get(Convert.toInt(userId)).equals(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID)){
            imChatUserMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_TRUE);
        }else{
            imChatUserMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_FALSE);
        }
        return imChatUserMessage;
    }

    /**
     * @apiNote 创建群通知消息实例
     * @param group ImChatGroup实例
     * @param content 消息内容
     * @return 群聊消息实例
     */
    public static ImChatUserGroupMessage builderMessage(ImChatGroup group,String content)
    {
        ImChatUserGroupMessage imChatUserGroupMessage=new ImChatUserGroupMessage();
        imChatUserGroupMessage.setGroupId(group.getId());
        imChatUserGroupMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        imChatUserGroupMessage.setToId(group.getId().toString());
        imChatUserGroupMessage.setCreateTime(LocalDateTime.now());
        imChatUserGroupMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_FALSE);
        imChatUserGroupMessage.setContent(content);
        return imChatUserGroupMessage;
    }

    public static ImChatUserGroupMessage builderMessage(String groupId,String uid,String content)
    {
        ImChatUserGroupMessage imChatUserGroupMessage=new ImChatUserGroupMessage();
        imChatUserGroupMessage.setGroupId(Convert.toInt(uid));
        imChatUserGroupMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        imChatUserGroupMessage.setToId(groupId);
        imChatUserGroupMessage.setCreateTime(LocalDateTime.now());
        imChatUserGroupMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_FALSE);
        imChatUserGroupMessage.setContent(content);
        return imChatUserGroupMessage;
    }



    public static void removeGroup(Integer groupId,Integer userId) {
        TioWebsocketStarter tioWebsocketStarter=TioUtil.getTio();
        if(tioWebsocketStarter!=null){
            Tio.removeGroup(tioWebsocketStarter.getServerTioConfig(),groupId.toString(),userId.toString());
        }
    }

    public static void bindGroup(Integer groupId,Integer userId){
        TioWebsocketStarter tioWebsocketStarter=TioUtil.getTio();
        if(tioWebsocketStarter!=null){
            Tio.bindGroup(tioWebsocketStarter.getServerTioConfig(),groupId.toString(),userId.toString());
        }
    }

    public static ImMessage getImMessage(ImChatUserGroupMessage message){
        ImMessage imMessage=new ImMessage();
        //FIXME 后续可将头像 替换成系统的头像 地址
        imMessage.setUsername("StellarWMS系统提醒");
        imMessage.setAvatar("");
        imMessage.setContent(message.getContent());
        imMessage.setGroupId(message.getGroupId());
        imMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        imMessage.setToId(Convert.toInt(message.getToId()));
        imMessage.setMessageType(ImEnum.MESSAGE_TYPE_MSG_SINGLE.getCode());
        return imMessage;
    }

    public static ImMessage getImMessage(ImChatUserMessage message){
        ImMessage imMessage=new ImMessage();
        //FIXME 后续可将头像 替换成系统的头像 地址
        imMessage.setUsername("StellarWMS系统提醒");
        imMessage.setAvatar("");
        imMessage.setContent(message.getContent());
        imMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        imMessage.setToId(Convert.toInt(message.getToId()));
        imMessage.setMessageType(ImEnum.MESSAGE_TYPE_MSG_SINGLE.getCode());
        return imMessage;
    }
}
