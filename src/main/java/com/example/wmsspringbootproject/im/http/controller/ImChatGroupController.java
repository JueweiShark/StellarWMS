package com.example.wmsspringbootproject.im.http.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.Utils.RedisUtil;
import com.example.wmsspringbootproject.Utils.SecurityUtils;
import com.example.wmsspringbootproject.Utils.SysPushMessage;
import com.example.wmsspringbootproject.common.Annotation.LogNote;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.core.security.model.SysUserDetails;
import com.example.wmsspringbootproject.im.http.entity.ImChatGroup;
import com.example.wmsspringbootproject.im.http.entity.ImChatGroupUser;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserGroupMessage;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import com.example.wmsspringbootproject.im.http.service.ImChatGroupService;
import com.example.wmsspringbootproject.im.http.service.ImChatGroupUserService;
import com.example.wmsspringbootproject.im.http.service.ImChatUserGroupMessageService;
import com.example.wmsspringbootproject.im.http.service.ImChatUserMessageService;
import com.example.wmsspringbootproject.im.http.vo.GroupUserVO;
import com.example.wmsspringbootproject.im.http.vo.GroupVO;
import com.example.wmsspringbootproject.im.websocket.ImEnum;
import com.example.wmsspringbootproject.im.websocket.TioUtil;
import com.example.wmsspringbootproject.im.websocket.TioWebsocketStarter;
import com.example.wmsspringbootproject.im.websocket.constants.ImConfigConst;
import com.example.wmsspringbootproject.model.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.tio.core.Tio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "04.聊天群管理")
@CrossOrigin

@RequestMapping("/group")
public class ImChatGroupController {

    @Autowired
    private ImChatGroupUserService imChatGroupUserService;

    @Autowired
    private ImChatGroupService imChatGroupService;

    @Autowired
    private ImChatUserGroupMessageService imChatUserGroupMessageService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ImChatUserMessageService imChatUserMessageService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    @Operation(summary = "获取群列表")
    @LogNote(description="获取群列表")
    public Result<List<GroupVO>> list() {
        Long userId= SecurityUtils.getUserId();
        LambdaQueryWrapper<ImChatGroupUser> lambdaQuery=new LambdaQueryWrapper<>();
        if(!SecurityUtils.isAdmin()&&!SecurityUtils.isRoot()){
            lambdaQuery.eq(ImChatGroupUser::getUserId,userId);
        }
        lambdaQuery.in(ImChatGroupUser::getUserStatus, ImConfigConst.GROUP_USER_STATUS_PASS,ImConfigConst.GROUP_USER_STATUS_SILENCE);
        List<ImChatGroupUser> groupUsers=imChatGroupUserService.list(lambdaQuery);

        Map<Integer,ImChatGroupUser> groupUserMap=groupUsers.stream().collect(Collectors.toMap(ImChatGroupUser::getGroupId, Function.identity()));

        LambdaQueryWrapper<ImChatGroup> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ImChatGroup::getGroupType,ImConfigConst.GROUP_TOPIC);
        if(CollectionUtil.isNotEmpty(groupUserMap)){
            wrapper.or(g->g.in(ImChatGroup::getId,groupUserMap.keySet()))
                    .eq(ImChatGroup::getGroupType,ImConfigConst.GROUP_COMMON);
        }
        List<ImChatGroup> groups=imChatGroupService.list(wrapper);

        List<GroupVO> groupVOS=groups.stream().map(group->{
            ImChatGroupUser imChatGroupUser=groupUserMap.get(group.getId());
            ImChatGroupUser groupUser=new ImChatGroupUser();
            if(imChatGroupUser!=null){
                groupUser.setUserStatus(imChatGroupUser.getUserStatus());
                groupUser.setCreateTime(LocalDateTime.now());
                groupUser.setUserId(Integer.parseInt(String.valueOf(userId)));
                groupUser.setAdminFlag(imChatGroupUser.getAdminFlag());
            }
            return getGroupVO(group,groupUser);
        }).toList();
        return Result.success(groupVOS);
    }

    public GroupVO getGroupVO(ImChatGroup imChatGroup,ImChatGroupUser imChatGroupUser){
        GroupVO groupVO=new GroupVO();
        groupVO.setGroupName(imChatGroup.getGroupName());
        groupVO.setGroupType(imChatGroup.getGroupType());
        groupVO.setId(imChatGroup.getId());
        groupVO.setCreateTime(imChatGroupUser.getCreateTime());
        groupVO.setAvatar(imChatGroup.getAvatar());
        groupVO.setIntroduction(imChatGroup.getIntroduction());
        groupVO.setNotice(imChatGroup.getNotice());
        groupVO.setAdminFlag(imChatGroupUser.getAdminFlag());
        groupVO.setInType(imChatGroup.getInType());
        groupVO.setUserStatus(imChatGroupUser.getUserStatus());
        groupVO.setMasterFlag(imChatGroup.getMasterUserId().intValue()==imChatGroupUser.getUserId());
        return groupVO;
    }

    @PostMapping("/create")
    @LogNote(description="创建群聊")
    @Operation(summary="创建群聊")
    public Result<Boolean> createGroup(@RequestBody ImChatGroup imChatGroup){
        if(!StringUtils.hasText(imChatGroup.getGroupName())){
            return Result.failed("群名称不能为空");
        }
        Long userId= SecurityUtils.getUserId();
        imChatGroup.setMasterUserId(Integer.parseInt(String.valueOf(userId)));
        imChatGroup.setCreateTime(LocalDateTime.now());
        if(imChatGroup.getGroupType()==ImConfigConst.GROUP_TOPIC){
            imChatGroup.setInType(ImConfigConst.IN_TYPE_FALSE);
        }
        imChatGroupService.save(imChatGroup);

        ImChatGroup group=imChatGroupService.getOne(new LambdaQueryWrapper<ImChatGroup>()
                .eq(ImChatGroup::getGroupName,imChatGroup.getGroupName()));
        ImChatGroupUser imChatGroupUser=new ImChatGroupUser();
        imChatGroupUser.setGroupId(group.getId());
        imChatGroupUser.setUserStatus(ImConfigConst.GROUP_USER_STATUS_PASS);
        imChatGroupUser.setAdminFlag(false);
        imChatGroupUser.setRemark(SecurityUtils.getUser().getName());
        imChatGroupUser.setCreateTime(LocalDateTime.now());
        imChatGroupUser.setVerifyUserId(ImConfigConst.GROUP_DEFAULT_VERIFY_USER_ID);

        imChatGroupUserService.save(imChatGroupUser);

        TioWebsocketStarter tioWebsocketStarter= TioUtil.getTio();
        if(tioWebsocketStarter!=null){
            Tio.bindGroup(tioWebsocketStarter.getServerTioConfig(),String.valueOf(userId),String.valueOf(imChatGroup.getId()));
        }
        return Result.success();
    }

    @GetMapping("/join")
    @LogNote(description="加入群聊")
    @Operation(summary="加入群聊")
    public Result<Boolean> joinGroup(@RequestParam Integer groupId){
        SysUserDetails user = SecurityUtils.getUser();
        ImChatGroup group=imChatGroupService.getById(groupId);

        if(group==null){
            return Result.failed("该群聊不存在");
        }
        if(group.getGroupType()==ImConfigConst.GROUP_TOPIC){
            TioWebsocketStarter tioWebsocketStarter=TioUtil.getTio();
            if(tioWebsocketStarter!=null){
                Tio.bindGroup(tioWebsocketStarter.getServerTioConfig(), String.valueOf(user.getId()), group.getId().toString());
            }
            insertImChatGroupUser(group,user.getName(),user.getId(),ImConfigConst.GROUP_USER_STATUS_PASS);
            return Result.result("200","加入话题成功！",true);
        }else{
            ImChatGroupUser userFromDB=imChatGroupUserService.getOne(new LambdaQueryWrapper<ImChatGroupUser>()
                    .eq(ImChatGroupUser::getGroupId,group.getId())
                    .eq(ImChatGroupUser::getUserId,user.getId()));
            if(userFromDB!=null){
                return Result.failed("禁止重复申请");
            }
            if(group.getInType()==ImConfigConst.IN_TYPE_TRUE){

                systemNotify(group,user.getId(),user.getUsername());
                insertImChatGroupUser(group,user.getUsername(),user.getId(),ImConfigConst.GROUP_USER_STATUS_NOT_VERIFY);
                return Result.result("200","申请成功！待审核！",true);
            }else{
                insertImChatGroupUser(group,user.getUsername(),user.getId(),ImConfigConst.GROUP_USER_STATUS_PASS);
                return Result.result("200","加入群聊成功！",true);
            }
        }
    }

    public void systemNotify(ImChatGroup group,Integer userId,String userName){
        List<ImChatGroupUser> groupAdmins=imChatGroupUserService.list(new LambdaQueryWrapper<ImChatGroupUser>()
                .eq(ImChatGroupUser::getGroupId,group.getId())
                .eq(ImChatGroupUser::getAdminFlag,ImConfigConst.ADMIN_FLAG_TRUE));

        ImChatUserGroupMessage imChatUserGroupMessage=new ImChatUserGroupMessage();
        //通知群主和管理员的消息
        imChatUserGroupMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_FALSE);
        imChatUserGroupMessage.setContent("申请："+userName+"申请加入"+group.getGroupName()+"群聊");
        imChatUserGroupMessage.setCreateTime(LocalDateTime.now());
        imChatUserGroupMessage.setGroupId(group.getId());
        imChatUserGroupMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        String toIds=Convert.toStr(group.getMasterUserId());
        for (ImChatGroupUser groupAdmin : groupAdmins) {
            toIds=toIds+","+groupAdmin.getId();
        }
        imChatUserGroupMessage.setToId(toIds);
        if(imChatUserGroupMessage.getToId().contains(",")){
            for (String s : imChatUserGroupMessage.getToId().split(",")) {
                SysPushMessage.send(s,imChatUserGroupMessage,group.getId());
            }
        }else{
            SysPushMessage.send(imChatUserGroupMessage.getToId(),imChatUserGroupMessage,group.getId());
        }
        //通知申请者的消息
        ImChatUserGroupMessage selfMessage=JSON.parseObject(JSON.toJSONString(imChatUserGroupMessage),ImChatUserGroupMessage.class);
        selfMessage.setToId(String.valueOf(userId));
        selfMessage.setContent(group.getGroupName()+"的申请");
        List<ImChatUserGroupMessage> messages=new ArrayList<>();
        messages.add(imChatUserGroupMessage);
        messages.add(selfMessage);

        imChatUserGroupMessageService.saveBatch(messages);

        SysPushMessage.send(selfMessage.getToId(),selfMessage,group.getId());
    }


    /**
     * @apiNote 保存群成员信息
     * @param group 群实例
     * @param userName 用户名称
     * @param uid 用户id
     * @param status 成员状态
     */
    public  void insertImChatGroupUser(ImChatGroup group,String userName,Integer uid, Integer status){
        ImChatGroupUser imChatGroupUser=imChatGroupUserService.lambdaQuery()
                .eq(ImChatGroupUser::getUserId,uid)
                .eq(ImChatGroupUser::getGroupId,group.getId())
                .eq(ImChatGroupUser::getUserStatus,ImConfigConst.GROUP_USER_STATUS_BAN).one();
        String flag="update";
        if(imChatGroupUser==null){
            imChatGroupUser=new ImChatGroupUser();
            imChatGroupUser.setUserId(uid);
            imChatGroupUser.setRemark(userName);
            imChatGroupUser.setAdminFlag(ImConfigConst.ADMIN_FLAG_FALSE);
            imChatGroupUser.setCreateTime(LocalDateTime.now());
            imChatGroupUser.setGroupId(group.getId());
            imChatGroupUser.setVerifyUserId(ImConfigConst.GROUP_DEFAULT_VERIFY_USER_ID);
            flag="save";
        }
        imChatGroupUser.setUserStatus(status);
        if(status==1){
            if(flag.equals("update")){
                imChatGroupUserService.updateById(imChatGroupUser);
            }else{
                imChatGroupUserService.save(imChatGroupUser);
            }
        }else{
            redisUtil.set(imChatGroupUser.getUserId().toString()+"_"+imChatGroupUser.getGroupId()+"_"+flag,imChatGroupUser,ImConfigConst.REDIS_EXPIRE_TIME);
        }
    }

    @GetMapping("/dissolve")
    @LogNote(description="解散群聊")
    @Operation(summary = "解散群聊")
    public Result<Boolean> dissolveGroup(@RequestParam Integer groupId){
        ImChatGroup group=imChatGroupService.getById(groupId);
        SysUserDetails userDetails=SecurityUtils.getUser();
        if(group==null){
            return Result.failed("群聊不存在");
        }
        boolean isSuccess=false;
        if(userDetails.getId()==group.getMasterUserId()||SecurityUtils.isAdmin()||SecurityUtils.isRoot()){
            isSuccess=imChatGroupService.removeById(groupId);
        }
        if(isSuccess){
            LambdaUpdateChainWrapper<ImChatGroupUser> chainWrapper=imChatGroupUserService.lambdaUpdate();
            chainWrapper.eq(ImChatGroupUser::getGroupId,groupId).remove();

            LambdaUpdateChainWrapper<ImChatUserGroupMessage> wrapper=imChatUserGroupMessageService.lambdaUpdate();
            wrapper.eq(ImChatUserGroupMessage::getGroupId,groupId).remove();

            return Result.result("200","解散群聊成功",true);
        }
        return Result.failed("群聊解散失败");
    }

    @PutMapping("/modify")
    @LogNote(description="修改群聊信息")
    @Operation(summary="修改群聊信息")
    public Result<ImChatGroup> modifyGroup( @ParameterObject ImChatGroup imChatGroup
    ){
        ImChatGroup group=imChatGroupService.getById(imChatGroup.getId());
        if(group==null){
            return Result.failed("群聊不存在");
        }
        Long count=imChatGroupUserService.lambdaQuery().eq(ImChatGroupUser::getUserId,SecurityUtils.getUserId())
                .eq(ImChatGroupUser::getAdminFlag,ImConfigConst.ADMIN_FLAG_TRUE).count();

        if(Objects.equals(SecurityUtils.getUserId(),group.getMasterUserId())||count==1){
            if(StrUtil.isNotEmpty(imChatGroup.getGroupName())){
                imChatGroupService.updateById(imChatGroup);
                return Result.result("200","修改群聊信息成功",imChatGroup);
            }else{
                return Result.failed("群名称不能为空");
            }
        }else{
            return Result.failed("无权限修改");
        }
    }

    //TODO 审核入群申请
    @PostMapping("/verify")
    @LogNote(description="审核入群申请[isPass: 1：审核通过，2：审核不通过]")
    @Operation(summary="审核入群申请[isPass: 1：审核通过，2：审核不通过]")
    public Result<Boolean> verifyGroupUser(@RequestParam("groupId")String groupId, @RequestParam("userId")String userId
    ,@RequestParam("isPass")String isPass){
        ImChatGroup imChatGroup=imChatGroupService.lambdaQuery().eq(ImChatGroup::getId,groupId).one();
        if(imChatGroup==null){
            return Result.failed("群聊不存在");
        }
        ImChatGroupUser imChatGroupUser=imChatGroupUserService.lambdaQuery()
                .eq(ImChatGroupUser::getUserId,userId)
                .eq(ImChatGroupUser::getGroupId,groupId)
                .eq(ImChatGroupUser::getUserStatus,ImConfigConst.GROUP_USER_STATUS_NOT_VERIFY).one();
        String content="";
        if(imChatGroupUser!=null){
            imChatGroupUserService.lambdaUpdate()
                    .eq(ImChatGroupUser::getUserId,userId)
                    .eq(ImChatGroupUser::getGroupId,groupId)
                    .set(ImChatGroupUser::getUserStatus,Convert.toInt(isPass)==1 ?
                            ImConfigConst.GROUP_USER_STATUS_PASS:ImConfigConst.GROUP_USER_STATUS_BAN).update();
        }else{
            imChatGroupUser=redisUtil.get(userId+"+"+groupId+"_save");
            if(imChatGroupUser==null){
                //通知申请审核逾期
                content="您对于"+imChatGroup.getGroupName()+"群聊的入群申请逾期未通过";
                ImChatUserGroupMessage imChatUserMessage=SysPushMessage.builderMessage(imChatGroupUser.getGroupId().toString(),userId,content);
                SysPushMessage.send(userId,imChatUserMessage, imChatGroupUser.getGroupId());
                return Result.failed("申请已经过期");
            }else{
                imChatGroupUser.setUserStatus(ImConfigConst.GROUP_USER_STATUS_PASS);
                imChatGroupUserService.save(imChatGroupUser);
                redisUtil.del(userId+"+"+groupId+"_save");
                //通知申请审核通过
                content="您对于"+imChatGroup.getGroupName()+"群聊的入群申请已通过";
                ImChatUserGroupMessage imChatUserMessage=SysPushMessage.builderMessage(imChatGroupUser.getGroupId().toString(),userId,content);
                SysPushMessage.send(userId,imChatUserMessage, imChatGroupUser.getGroupId());
                return Result.success();
            }
        }
        return Result.failed("服务器异常");
    }

    //TODO: 获取群申请列表
    @GetMapping("/applications")
    @LogNote(description = "获取群申请列表")
    @Operation(summary="获取群申请列表")
    public Result<List<GroupUserVO>> getApplications(){

        List<ImChatGroupUser> imChatGroupUsers=imChatGroupUserService.lambdaQuery()
                .eq(ImChatGroupUser::getUserId,SecurityUtils.getUserId())
                .eq(ImChatGroupUser::getAdminFlag,ImConfigConst.ADMIN_FLAG_TRUE)
                .select(ImChatGroupUser::getGroupId).list();

        List<ImChatGroup> chatGroups=imChatGroupService.lambdaQuery()
                .eq(ImChatGroup::getMasterUserId,SecurityUtils.getUserId())
                .select(ImChatGroup::getId).list();

        List<Integer> groupIds=null;
        if(imChatGroupUsers != null && chatGroups != null) {
            groupIds = imChatGroupUsers.stream().map(ImChatGroupUser::getGroupId).toList();
            groupIds.addAll(chatGroups.stream().map(ImChatGroup::getId).toList());
        }else if (imChatGroupUsers!=null){
            groupIds = imChatGroupUsers.stream().map(ImChatGroupUser::getGroupId).toList();
        }else if(chatGroups!=null){
            groupIds = chatGroups.stream().map(ImChatGroup::getId).toList();
        }
        if(groupIds!=null){
            List<ImChatGroupUser> applications = imChatGroupUserService.lambdaQuery()
                    .eq(ImChatGroupUser::getUserStatus, ImConfigConst.GROUP_USER_STATUS_NOT_VERIFY)
                    .in(ImChatGroupUser::getGroupId, groupIds).list();

            List<GroupUserVO> userVOList=applications.stream().map(item->{
                GroupUserVO vo=getGroupUserVO(item,imChatGroupService.lambdaQuery().
                        eq(ImChatGroup::getId,item.getGroupId()).one(),userService.lambdaQuery()
                        .eq(Users::getId,item.getUserId()).one());
                return vo;
            }).toList();

            return Result.success(userVOList);
        }
        return Result.failed("暂无数据");
    }

    public GroupUserVO getGroupUserVO(ImChatGroupUser imChatGroupUser,ImChatGroup imChatGroup,Users users){
        GroupUserVO groupUserVO=new GroupUserVO();
        groupUserVO.setUserId(imChatGroupUser.getUserId());
        groupUserVO.setGroupName(imChatGroup.getGroupName());
        groupUserVO.setUsername(users.getName());
        groupUserVO.setGroupId(imChatGroup.getId());
        groupUserVO.setVerifyUserId(imChatGroupUser.getVerifyUserId());
        groupUserVO.setUserStatus(imChatGroupUser.getUserStatus());
        groupUserVO.setAvatar(users.getAvatar());
        groupUserVO.setRemark(imChatGroupUser.getRemark());
        groupUserVO.setCreateTime(imChatGroupUser.getCreateTime());
        groupUserVO.setAdminFlag(imChatGroupUser.getAdminFlag());
        return groupUserVO;
    }

}
