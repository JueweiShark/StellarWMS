package com.example.wmsspringbootproject.im.http.controller;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wmsspringbootproject.Utils.*;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.core.security.model.SysUserDetails;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserGroupMessage;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import com.example.wmsspringbootproject.im.http.service.ImChatUserGroupMessageService;
import com.example.wmsspringbootproject.im.http.service.ImChatUserMessageService;
import com.example.wmsspringbootproject.im.http.vo.UserMessageVO;
import com.example.wmsspringbootproject.im.websocket.ImMessage;
import com.example.wmsspringbootproject.im.websocket.constants.ImConfigConst;
import com.example.wmsspringbootproject.model.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Tag(name = "02.单聊消息管理")
@CrossOrigin

@RequestMapping("/user_message")
public class ImChatUserMessageController {

    @Autowired
    private ImChatUserMessageService imChatUserMessageService;

    @Autowired
    private ImChatUserGroupMessageService imChatUserGroupMessageService;

    @Autowired
    private CommonQuery commonQuery;

    //TODO: 获取系统消息
    @GetMapping("/system")
    @Operation(summary = "获取系统消息列表")
    public Result<List<ImMessage>> getSystemMessage() {
        Long uid= SecurityUtils.getUserId();
        List<ImChatUserMessage> messages=imChatUserMessageService.lambdaQuery()
                .eq(ImChatUserMessage::getToId,uid)
                .eq(ImChatUserMessage::getFromId, ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID).list();

        List<ImChatUserGroupMessage> groupMessages=imChatUserGroupMessageService.lambdaQuery()
                .eq(ImChatUserGroupMessage::getToId,uid)
                .eq(ImChatUserGroupMessage::getFromId, ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID).list();
        List<ImMessage> imMessageList=new ArrayList<>(groupMessages.stream()
                .map(SysPushMessage::getImMessage).toList());

        imMessageList.addAll(messages.stream().map(SysPushMessage::getImMessage).toList());
        return Result.success(imMessageList);
    }
    //TODO: 获取用户消息
    @GetMapping("/friend")
    @Operation(summary = "获取用户消息列表")
    public Result<Page<UserMessageVO>> getUserMessage(@RequestParam("id")String id,
                                                      @RequestParam("currentTime")String currentTime,
                                                      @RequestParam("size")String size,
                                                      @RequestParam("page")String page){
        Long uid= SecurityUtils.getUserId();
        SysUserDetails currentUsers=SecurityUtils.getUser();
        Page<ImChatUserMessage> imChatUserMessagePage=new Page<>(Integer.parseInt(page),Integer.parseInt(size));
        Page<ImChatUserMessage> Messages=imChatUserMessageService.lambdaQuery()
                .and(wrapper->
                        wrapper.eq(ImChatUserMessage::getToId,uid)
                                .eq(ImChatUserMessage::getFromId,id))
                .or(query->
                        query.eq(ImChatUserMessage::getToId,id)
                                .eq(ImChatUserMessage::getFromId,uid))
                .ge(ImChatUserMessage::getCreateTime,TextUtil.getBeforeAnyTime(null, Constants.TimeValueInMillions.MONTH))
                .le(ImChatUserMessage::getCreateTime,currentTime)
                .orderByDesc(ImChatUserMessage::getCreateTime).page(imChatUserMessagePage);

        Users users=commonQuery.getUser(Integer.parseInt(id));

        List<UserMessageVO> userMessageVOList=Messages.getRecords().stream().map(result->{
            UserMessageVO userMessageVO=new UserMessageVO();
            userMessageVO.setId(result.getId());
            userMessageVO.setMessageStatus(result.getMessageStatus());
            userMessageVO.setContent(result.getContent());
            userMessageVO.setFromId(result.getFromId());
            userMessageVO.setToId(result.getToId());
            userMessageVO.setAvatar(
                    Objects.equals(Convert.toLong(userMessageVO.getFromId()),uid) ? currentUsers.getAvatar() : users.getAvatar()
            );
            return userMessageVO;
        }).toList();

        Page<UserMessageVO> userMessageVOPage=new Page<>();
        userMessageVOPage.setRecords(userMessageVOList);
        userMessageVOPage.setCurrent(Messages.getCurrent());
        userMessageVOPage.setTotal(Messages.getTotal());

        return Result.success(userMessageVOPage);
    }

    //TODO: 添加单聊消息
    @PostMapping("/add")
    @Operation(summary = "添加单聊消息")
    public Result<Boolean> addUserMessage(@RequestBody ImChatUserMessage imChatUserMessage){
        if(TextUtil.textIsEmpty(imChatUserMessage.getContent())){
            return Result.failed("消息内容不能为空");
        }
        if(Objects.isNull(imChatUserMessage.getToId())){
            return Result.failed("接收人不能为空");
        }
        imChatUserMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        if(Objects.isNull(imChatUserMessage.getMessageStatus())){
            imChatUserMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_FALSE);
        }
        imChatUserMessage.setCreateTime(LocalDateTime.now());
        boolean result=imChatUserMessageService.save(imChatUserMessage);
        SysPushMessage.send(imChatUserMessage);
        return result ? Result.success(result) : Result.failed("添加单聊消息失败");
    }
    //TODO: 删除单聊消息
    @DeleteMapping("/delete")
    @Operation(summary = "删除单聊消息")
    public Result<Boolean> deleteUserMessage(@RequestParam("id")String id){
        if(TextUtil.textIsEmpty(id)){
            return Result.failed("消息id不能为空");
        }
        ImChatUserMessage imChatUserMessage=imChatUserMessageService.lambdaQuery()
                .eq(ImChatUserMessage::getFromId,ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID)
                .eq(ImChatUserMessage::getId,id)
                .one();
        if(Objects.isNull(imChatUserMessage)){
            return Result.failed("消息不存在");
        }
        boolean result=imChatUserMessageService.removeById(id);
        return result? Result.success(result) : Result.failed("删除单聊消息失败");
    }
}
