package com.example.wmsspringbootproject.im.http.controller;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.Utils.CommonQuery;
import com.example.wmsspringbootproject.Utils.SecurityUtils;
import com.example.wmsspringbootproject.Utils.SysPushMessage;
import com.example.wmsspringbootproject.Utils.WmsCache;
import com.example.wmsspringbootproject.common.Annotation.LogNote;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.im.http.entity.ImChatGroup;
import com.example.wmsspringbootproject.im.http.entity.ImChatGroupUser;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserGroupMessage;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import com.example.wmsspringbootproject.im.http.service.ImChatGroupService;
import com.example.wmsspringbootproject.im.http.service.ImChatGroupUserService;
import com.example.wmsspringbootproject.im.http.vo.GroupUserVO;
import com.example.wmsspringbootproject.im.websocket.MessageCache;
import com.example.wmsspringbootproject.im.websocket.TioUtil;
import com.example.wmsspringbootproject.im.websocket.TioWebsocketStarter;
import com.example.wmsspringbootproject.im.websocket.constants.ImConfigConst;
import com.example.wmsspringbootproject.model.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tio.core.Tio;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Tag(name = "05.聊天群成员管理")
@CrossOrigin

@RequestMapping("/group_user")
public class ImChatGroupUserController {
    @Autowired
    private ImChatGroupUserService imChatGroupUserService;

    @Autowired
    private ImChatGroupService imChatGroupService;

    @Autowired
    private ImChatGroupController imChatGroupController;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageCache messageCache;
    @Autowired
    private CommonQuery commonQuery;

    // TODO 将群成员踢出群聊

    /**
     * @apiNote 踢出群聊
     * @param id 群成员的 userId
     * @param groupId 群id
     */
    @DeleteMapping("/kickOut")
    @LogNote(description="踢出群聊")
    @Operation(summary = "踢出群聊")
    public Result<Boolean> kickOut(@RequestParam("id")String id, @RequestParam("groupId")String groupId) {
        Users users= commonQuery.getUser(Convert.toInt(id));
        if(users==null){
            return Result.failed("该用户不存在");
        }
        LambdaQueryChainWrapper<ImChatGroup> wrapper=imChatGroupService.lambdaQuery();
        ImChatGroup group=wrapper.eq(ImChatGroup::getId,groupId).one();
        if(group==null){
            return Result.failed("群聊不存在");
        }
        ImChatGroupUser imChatGroupUser=imChatGroupUserService.lambdaQuery()
                .eq(ImChatGroupUser::getUserId,id)
                .eq(ImChatGroupUser::getGroupId,groupId).one();
        if(imChatGroupUser==null){
            return Result.failed("该用户不在该群中");
        }

        if(identityCheck(groupId,SecurityUtils.getUserId(),2) || Objects.equals(SecurityUtils.getUserId(),group.getMasterUserId())){
            LambdaUpdateChainWrapper<ImChatGroupUser> query=imChatGroupUserService.lambdaUpdate();
            boolean result=query.set(ImChatGroupUser::getUserStatus, ImConfigConst.GROUP_USER_STATUS_BAN)
                    .eq(ImChatGroupUser::getUserId,id)
                    .eq(ImChatGroupUser::getGroupId,groupId)
                    .update();
            if(result){
                String content="您被"+SecurityUtils.getUser().getName()+"踢出了"+group.getGroupName()+"群聊";
                ImChatUserGroupMessage imChatUserGroupMessage=SysPushMessage.builderMessage(imChatGroupUser.getGroupId().toString(),id,content);
                SysPushMessage.send(id,imChatUserGroupMessage, imChatGroupUser.getGroupId());
                SysPushMessage.removeGroup(group.getId(),users.getId());
            }
            return Result.result("200",result?"踢出群聊操作成功":"操作失败，请稍后再试！",result);
        }
        return Result.failed("踢出群聊操作失败,没有权限");
    }
    // TODO 邀请新人入群

    /**
     *@apiNote 邀请亲人入群
     * @param userId 新人id
     * @param groupId 群id
     */
    @PostMapping("/invite")
    @LogNote(description="邀请新人入群")
    @Operation(summary = "邀请新人入群")
    public Result<Boolean> invite(@RequestParam("userId")String userId, @RequestParam("groupId")String groupId) {
        Long uid=SecurityUtils.getUserId();
        Long number=imChatGroupUserService.lambdaQuery()
                .eq(ImChatGroupUser::getUserId,uid)
                .eq(ImChatGroupUser::getGroupId,groupId).count();

        Users users=commonQuery.getUser(Convert.toInt(userId));
        if(users==null){
            return Result.failed("该用户不存在");
        }
        ImChatGroup group=imChatGroupService.getById(groupId);
        if(identityCheck(groupId,SecurityUtils.getUserId(),2) || Objects.equals(Convert.toLong(group.getMasterUserId()), uid)){
            imChatGroupController.insertImChatGroupUser(group,users.getName(),users.getId(),ImConfigConst.GROUP_USER_STATUS_PASS);
            //加入群聊通知
            String content1=SecurityUtils.getUser().getName()+"已成功邀请您加入"+group.getGroupName()+"群聊";
            ImChatUserGroupMessage imChatUserGroupMessage=SysPushMessage.builderMessage(groupId,userId,content1);
            SysPushMessage.send(userId,imChatUserGroupMessage,group.getId());

            //新成员加入群聊通知
            String content2=SecurityUtils.getUser().getName()+"邀请了"+users.getName()+"加入了群聊";
            ImChatUserGroupMessage imChatUserGroupMessage1=SysPushMessage.builderMessage(group,content2);
            SysPushMessage.send(imChatUserGroupMessage1);

            SysPushMessage.bindGroup(group.getId(),users.getId());

            messageCache.putGroupMessage(imChatUserGroupMessage);
            messageCache.putGroupMessage(imChatUserGroupMessage1);
            return Result.result("200","邀请成功!",true);
        }else if(number==1){
            imChatGroupController.insertImChatGroupUser(group,users.getName(),users.getId(),ImConfigConst.GROUP_USER_STATUS_NOT_VERIFY);
            imChatGroupController.systemNotify(group,users.getId(),users.getName());
            Result.result("200","邀请成功！可在系统消息出查看审核进度",true);
        }
        return Result.failed("没有权限操作");
    }
    // TODO 退出群聊

    /**
     * @apiNote 退出群聊
     * @param groupId 群id
     */
    @PutMapping("/exit")
    @LogNote(description="退出群聊")
    @Operation(summary="退出群聊")
    public Result<Boolean> exitGroup(@RequestParam("groupId")String groupId){
        LambdaUpdateChainWrapper<ImChatGroupUser> wrapper=imChatGroupUserService.lambdaUpdate();
        boolean result=wrapper.set(ImChatGroupUser::getUserStatus,ImConfigConst.GROUP_USER_STATUS_BAN)
                .eq(ImChatGroupUser::getUserId,SecurityUtils.getUserId())
                .eq(ImChatGroupUser::getGroupId,groupId).update();
        if(result){
            SysPushMessage.removeGroup(Convert.toInt(groupId),Convert.toInt(SecurityUtils.getUserId()));
        }
        return Result.result(result?"200":Result.failed().getCode(),
                result?"退出群聊成功":"退出群聊失败，请稍后重试",result);
    }
    // TODO 查询群成员详细信息

    /**
     * @apiNote 获取群成员详细信息
     * @param uid 用户id
     * @param groupId 群id
     */
    @GetMapping("/details")
    @LogNote(description="获取群成员详细信息")
    @Operation(summary = "获取群成员详细信息")
    public Result<GroupUserVO> getDetails(@RequestParam("uid")String uid,@RequestParam("groupId")String groupId){
        ImChatGroup group=imChatGroupService.getOne(new LambdaQueryWrapper<ImChatGroup>()
                .eq(ImChatGroup::getId,groupId));
        if(group==null){
            return Result.failed("群聊已被解散");
        }
        Users users=commonQuery.getUser(Convert.toInt(uid));
        if(users==null){
            return Result.failed("该用户不存在");
        }
        ImChatGroupUser imChatGroupUser=imChatGroupUserService.lambdaQuery().eq(ImChatGroupUser::getUserId,uid)
                .eq(ImChatGroupUser::getGroupId,groupId).one();
        if(imChatGroupUser==null){
            return Result.failed("该用户已不在群聊当中");
        }
        GroupUserVO groupUserVO = getGroupUserVO(imChatGroupUser, group, users);

        return Result.success(groupUserVO);
    }

    /**
     * @apiNote 获取群成员视图 对象
     * @param imChatGroupUser 群聊成员实例
     * @param group 群实例
     * @param users 用户实例
     */
    private GroupUserVO getGroupUserVO(ImChatGroupUser imChatGroupUser, ImChatGroup group, Users users) {
        GroupUserVO groupUserVO=new GroupUserVO();
        groupUserVO.setId(imChatGroupUser.getId());
        groupUserVO.setGroupId(imChatGroupUser.getGroupId());
        groupUserVO.setUserId(imChatGroupUser.getUserId());
        groupUserVO.setGroupName(group.getGroupName());
        groupUserVO.setUserStatus(imChatGroupUser.getUserStatus());
        groupUserVO.setVerifyUserId(imChatGroupUser.getVerifyUserId());
        groupUserVO.setAvatar(users.getAvatar());
        groupUserVO.setRemark(imChatGroupUser.getRemark());
        groupUserVO.setCreateTime(imChatGroupUser.getCreateTime());
        groupUserVO.setUsername(users.getName());
        groupUserVO.setAdminFlag(imChatGroupUser.getAdminFlag());
        return groupUserVO;
    }

    // TODO 获取群成员列表

    /**
     * @apiNote 获取群成员列表
     * @param groupId 群id
     */
    @GetMapping("/list")
    @LogNote(description="获取群成员列表")
    @Operation(summary = "获取群成员列表")
    public Result<List<GroupUserVO>> groupMemberInfoList(@RequestParam("groupId")String groupId){

        ImChatGroup group=imChatGroupService.getOne(new LambdaQueryWrapper<ImChatGroup>()
                .eq(ImChatGroup::getId,groupId));
        if(group==null){
            return Result.failed("该群聊已经解散");
        }
        LambdaQueryChainWrapper<ImChatGroupUser> wrapper=imChatGroupUserService.lambdaQuery();
        List<ImChatGroupUser> groupUserList=wrapper.eq(ImChatGroupUser::getGroupId,groupId).list();

        List<Users> usersList=userService.lambdaQuery()
                .select(Users::getAvatar,Users::getName)
                .in(Users::getId,groupUserList.stream().map(ImChatGroupUser::getUserId)).list();

        List<GroupUserVO> groupUserVOList=groupUserList.stream()
                .map(imChatGroupUser -> getGroupUserVO(imChatGroupUser, group, Objects.requireNonNull(usersList.stream()
                        .filter(users -> users.getId().equals(imChatGroupUser.getUserId())).findFirst().orElse(null))))
                .collect(Collectors.toList());
        return Result.success(groupUserVOList);
    }
    // TODO 群成员禁言 or 解除禁言

    /**
     * @apiNote 群成员禁言 or 解除禁言
     * @param groupId 群id
     * @param uIds 被操作的用户id集合
     * @param type 操作类型
     */
    @PutMapping("/silenceOrRelieve")
    @LogNote(description="群成员禁言 or 解除禁言")
    @Operation(summary = "群成员禁言 [多id用','隔开]")
    public Result<Boolean> groupMemberBan(@RequestParam("groupId")String groupId,@RequestParam("uIds")String uIds,@RequestParam("type")String type){
        if(!identityCheck(groupId,SecurityUtils.getUserId(),1) || !identityCheck(groupId,SecurityUtils.getUserId(),2)){
            return Result.failed("您没有权限进行转让操作");
        }

        LambdaUpdateChainWrapper<ImChatGroupUser> wrapper=imChatGroupUserService.lambdaUpdate();
        wrapper.set(ImChatGroupUser::getUserStatus,type.equals("1") ?
                        ImConfigConst.GROUP_USER_STATUS_SILENCE :ImConfigConst.GROUP_USER_STATUS_PASS)
                .eq(ImChatGroupUser::getGroupId,groupId);
        if(uIds.contains(",")){
            wrapper.in(ImChatGroupUser::getUserId, Arrays.stream(uIds.split(",")).toList());
        }else{
            wrapper.eq(ImChatGroupUser::getUserId,uIds);
        }
        boolean result=wrapper.update();
        return Result.result(result?"200":Result.failed().getCode(),
                result?"禁言成功":"禁言失败，请稍后后重试",result);
    }
    // TODO 修改群成员角色

    /**
     * @apiNote 修改群成员角色
     * @param groupId 群id
     * @param uid 被操作的用户id
     * @param role 角色
     */
    @PutMapping("/role")
    @LogNote(description="修改群成员角色")
    @Operation(summary = "修改群成员角色[role 参数：1（管理员）0：（普通成员）]")
    public Result<Boolean> groupMemberRole(@RequestParam("groupId")String groupId,@RequestParam("uid")String uid,@RequestParam("role")Integer role){
        ImChatGroup group=imChatGroupService.getOne(new LambdaQueryWrapper<ImChatGroup>()
                .eq(ImChatGroup::getId,groupId));
        if(!Objects.equals(group.getMasterUserId(),Convert.toInt(SecurityUtils.getUserId()))){
            return Result.failed("您没有权限进行转让操作");
        }
        LambdaUpdateChainWrapper<ImChatGroupUser> wrapper=imChatGroupUserService.lambdaUpdate();
        boolean result=wrapper.set(ImChatGroupUser::getAdminFlag,role==1 ?
                        ImConfigConst.ADMIN_FLAG_TRUE : ImConfigConst.ADMIN_FLAG_FALSE)
                .eq(ImChatGroupUser::getUserId,uid)
                .eq(ImChatGroupUser::getGroupId,groupId).update();
        Users users=commonQuery.getUser(Convert.toInt(uid));

        String content=SecurityUtils.getUser().getName()+"将"+users.getName()+(role==1?"设置为管理员":"取消管理员身份");
        ImChatUserGroupMessage imChatUserGroupMessage=SysPushMessage.builderMessage(group,content);
        SysPushMessage.send(imChatUserGroupMessage);

        return Result.result(result?"200":Result.failed().getCode(),
                result?"修改角色成功":"修改角色失败，请稍后后重试",result);
    }

    //TODO 转让群

    /**
     * @apiNote 转让群
     * @param groupId 群id
     * @param uid 被操作的用户id
     */
    @PutMapping("/transfer")
    @LogNote(description="转让群")
    @Operation(summary = "转让群")//FIXME 做消息通知
    public Result<Boolean> transferGroup(@RequestParam("groupId")String groupId,@RequestParam("uid")String uid){
        ImChatGroup group=imChatGroupService.lambdaQuery()
                .eq(ImChatGroup::getId,groupId).one();
        if(group==null){
            return Result.failed("该群聊不存在");
        }
        Users users=commonQuery.getUser(Convert.toInt(uid));
        if(users==null){
            return Result.failed("该用户不存在");
        }
        if(!Objects.equals(Convert.toInt(SecurityUtils.getUserId()),group.getMasterUserId())){
            return Result.failed("您没有操作权限哦");
        }
        Long count=imChatGroupUserService.lambdaQuery().eq(ImChatGroupUser::getGroupId,groupId)
                .eq(ImChatGroupUser::getUserId,uid).count();
        LambdaUpdateChainWrapper<ImChatGroup> wrapper=imChatGroupService.lambdaUpdate();
        boolean result=wrapper.set(ImChatGroup::getMasterUserId,uid)
                .eq(ImChatGroup::getId,groupId).update();
        if(count!=1){
            TioWebsocketStarter tioWebsocketStarter= TioUtil.getTio();
            Tio.bindGroup(tioWebsocketStarter.getServerTioConfig(),groupId,uid);
        }
        String content=SecurityUtils.getUser().getName()+"已经将群转让给"+users.getName();
        ImChatUserGroupMessage imChatUserGroupMessage=SysPushMessage.builderMessage(group,content);
        SysPushMessage.send(imChatUserGroupMessage);

        String content1=SecurityUtils.getUser().getName()+"已经将群转让给您，快去给群里的小伙伴打招呼吧！";
        ImChatUserGroupMessage imChatUserGroupMessage1=SysPushMessage.builderMessage(groupId,uid,content1);
        SysPushMessage.send(uid,imChatUserGroupMessage1,group.getId());
        return Result.result(result?"200":Result.failed().getCode(),
                result?"转让群成功":"转让群失败，请稍后后重试",result);
    }

    /**
     * @apiNote 身份检查
     * @param groupId 群id
     * @param userId 需要验证的用户is
     * @param model 验证模式 【1：验证是否是群主，2：验证是否是管理员】
     */
    public boolean identityCheck(String groupId,Long userId,Integer model){
        switch (model) {
            case 1 -> {
                LambdaQueryChainWrapper<ImChatGroup> wrapper = imChatGroupService.lambdaQuery();
                Long count = wrapper
                        .eq(ImChatGroup::getId, groupId)
                        .eq(ImChatGroup::getMasterUserId, userId).count();
                return count == 1;
            }
            case 2 -> {
                LambdaQueryChainWrapper<ImChatGroupUser> wrapper1 = imChatGroupUserService.lambdaQuery();
                Long count1 = wrapper1
                        .eq(ImChatGroupUser::getGroupId, groupId)
                        .eq(ImChatGroupUser::getUserId, userId)
                        .eq(ImChatGroupUser::getAdminFlag, ImConfigConst.ADMIN_FLAG_TRUE)
                        .count();
                return count1 == 1;
            }
            default -> {
                return false;
            }
        }
    }
}
