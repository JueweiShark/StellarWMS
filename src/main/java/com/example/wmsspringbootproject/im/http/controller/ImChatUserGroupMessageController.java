package com.example.wmsspringbootproject.im.http.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wmsspringbootproject.Utils.CommonQuery;
import com.example.wmsspringbootproject.Utils.SysPushMessage;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.im.http.entity.ImChatGroup;
import com.example.wmsspringbootproject.im.http.entity.ImChatGroupUser;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserGroupMessage;
import com.example.wmsspringbootproject.im.http.service.ImChatGroupService;
import com.example.wmsspringbootproject.im.http.service.ImChatGroupUserService;
import com.example.wmsspringbootproject.im.http.service.ImChatUserGroupMessageService;
import com.example.wmsspringbootproject.im.http.vo.GroupMessageVO;
import com.example.wmsspringbootproject.im.websocket.constants.ImConfigConst;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Tag(name = "01.群聊消息管理")
@CrossOrigin

@RequestMapping("/group_message")
public class ImChatUserGroupMessageController {

    @Autowired
    private ImChatUserGroupMessageService imChatUserGroupMessageService;

    @Autowired
    private ImChatGroupService imChatGroupService;

    @Autowired
    private CommonQuery commonQuery;

    //TODO: 获取群消息列表
    @GetMapping("/list")
    @Operation(summary="获取群消息列表")
    public Result<Page<GroupMessageVO>> groupMessageList(@RequestParam("groupId")String groupId,
                                   @RequestParam("page")Integer page,
                                   @RequestParam("size")Integer size,
                                   @RequestParam("currentTime")String currentTime
    ){
        LambdaQueryChainWrapper<ImChatUserGroupMessage> wrapper=imChatUserGroupMessageService.lambdaQuery();
        wrapper.eq(ImChatUserGroupMessage::getToId, groupId);

        wrapper.and(query->query.ge(ImChatUserGroupMessage::getCreateTime, TextUtil.getBeforeAnyTime(TextUtil.parestDate(currentTime,null), Constants.TimeValueInMillions.MONTH))
                .le(ImChatUserGroupMessage::getCreateTime,currentTime));
        Page<ImChatUserGroupMessage> userGroupMessagePage=wrapper.page(new Page<>(page,size));

        ImChatGroup group=imChatGroupService.lambdaQuery()
                .eq(ImChatGroup::getId,groupId).one();
        if(group==null){
            return Result.failed("群聊不存在");
        }
        List<GroupMessageVO> groupMessageVOList=userGroupMessagePage.getRecords().stream()
                .map(item->{
                    GroupMessageVO groupMessageVO=new GroupMessageVO();
                    groupMessageVO.setCreateTime(item.getCreateTime());
                    groupMessageVO.setContent(item.getContent());
                    groupMessageVO.setFromId(item.getFromId());
                    groupMessageVO.setId(item.getId());
                    groupMessageVO.setToId(Integer.parseInt(item.getToId()));
                    groupMessageVO.setAvatar(commonQuery.getUser(item.getFromId()).getAvatar());
                    groupMessageVO.setUsername(commonQuery.getUser(item.getFromId()).getName());
                    groupMessageVO.setGroupId(item.getGroupId());
                    return groupMessageVO;
                }).toList();
        Page<GroupMessageVO> groupMessageVOPage=new Page<>();
        groupMessageVOPage.setRecords(groupMessageVOList);
        groupMessageVOPage.setTotal(userGroupMessagePage.getTotal());
        groupMessageVOPage.setPages(userGroupMessagePage.getPages());
        groupMessageVOPage.setCurrent(userGroupMessagePage.getCurrent());
        groupMessageVOPage.setSize(userGroupMessagePage.getSize());

        return Result.success(groupMessageVOPage);
    }
    //TODO: 添加群消息
    @PostMapping("/add")
    @Operation(summary="添加群消息")
    public Result<Boolean> addGroupMessage(@RequestBody ImChatUserGroupMessage message){
        if(TextUtil.textIsEmpty(message.getContent())){
            return Result.failed("消息内容不能为空");
        }
        if(TextUtil.textIsEmpty(message.getToId())){
            return Result.failed("接收人不能为空");
        }
        ImChatGroup group=imChatGroupService.lambdaQuery()
                        .eq(ImChatGroup::getId,message.getGroupId()).one();
        if(Objects.isNull(group)){
            return Result.failed("群聊不存在");
        }
        message.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_FALSE);
        message.setCreateTime(LocalDateTime.now());
        message.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);

        boolean result=imChatUserGroupMessageService.save(message);
        SysPushMessage.send(message);
        return result ? Result.success(result) : Result.failed("添加群聊消息失败");
    }
    //TODO: 删除群消息
    @DeleteMapping("/delete")
    @Operation(summary="删除群消息")
    public Result<Boolean> deleteGroupMessage(@RequestParam("msId")String msId){
        if(TextUtil.textIsEmpty(msId)){
            return Result.failed("消息id不能为空");
        }
        ImChatUserGroupMessage imChatUserGroupMessage=imChatUserGroupMessageService.lambdaQuery()
                .eq(ImChatUserGroupMessage::getId,msId).one();
        if(Objects.isNull(imChatUserGroupMessage)){
            return Result.failed("消息不存在");
        }
        boolean result=imChatUserGroupMessageService.removeById(msId);
        return result? Result.success(result) : Result.failed("删除群聊消息失败");
    }
}
