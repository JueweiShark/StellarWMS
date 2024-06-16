package com.example.wmsspringbootproject.im.http.controller;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.wmsspringbootproject.Utils.CommonQuery;
import com.example.wmsspringbootproject.Utils.RedisUtil;
import com.example.wmsspringbootproject.Utils.SecurityUtils;
import com.example.wmsspringbootproject.Utils.SysPushMessage;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserFriend;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import com.example.wmsspringbootproject.im.http.service.ImChatUserFriendService;
import com.example.wmsspringbootproject.im.http.vo.UserFriendVO;
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

@RestController
@RequiredArgsConstructor
@Tag(name = "03.用户好友管理")
@CrossOrigin

@RequestMapping("/user_friend")
public class ImChatUserFriendController {

    @Autowired
    private ImChatUserFriendService friendService;

    @Autowired
    private CommonQuery commonQuery;

    @Autowired
    private RedisUtil redisUtil;

    //TODO 删除好友
    /**
     * @apiNote 删除好友
     * @param friendId 好友userId
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除好友")
    public Result<Boolean> deleteFriend(@RequestParam("friendId")String friendId){
        Long uid=SecurityUtils.getUserId();
        ImChatUserFriend chatUserFriend=friendService.lambdaQuery().eq(ImChatUserFriend::getFriendId,friendId)
                .eq(ImChatUserFriend::getUserId,uid).one();
        if(chatUserFriend==null){
            return Result.failed("他不是您的好友");
        }
        Boolean result=friendService.lambdaUpdate().set(ImChatUserFriend::getFriendStatus, ImConfigConst.FRIEND_STATUS_BAN)
                .eq(ImChatUserFriend::getFriendId,friendId)
                .eq(ImChatUserFriend::getUserId,uid).update();
        Boolean result1=friendService.lambdaUpdate().set(ImChatUserFriend::getFriendStatus, ImConfigConst.FRIEND_STATUS_BAN)
                .eq(ImChatUserFriend::getFriendId,uid)
                .eq(ImChatUserFriend::getUserId,friendId).update();
        if(result1&&result){
            return Result.result("200","删除好友成功",true);
        }else{
            return Result.failed("删除好友失败");
        }
    }
    //TODO 添加好友
    @PostMapping("/add")
    @Operation(summary = "添加好友")
    public Result<Boolean> addFriend(@RequestParam("friendId")String friendId){
        Long uid=SecurityUtils.getUserId();
        ImChatUserFriend chatUserFriend=friendService.lambdaQuery()
                .eq(ImChatUserFriend::getFriendId,friendId)
                .eq(ImChatUserFriend::getUserId,uid)
                .eq(ImChatUserFriend::getFriendStatus,ImConfigConst.FRIEND_STATUS_BAN).one();
        if(chatUserFriend!=null){
            friendService.lambdaUpdate().eq(ImChatUserFriend::getUserId,uid)
                    .eq(ImChatUserFriend::getFriendId,friendId)
                    .set(ImChatUserFriend::getFriendStatus,ImConfigConst.FRIEND_STATUS_NOT_VERIFY)
                    .update();
            friendService.lambdaUpdate().eq(ImChatUserFriend::getUserId,friendId)
                    .eq(ImChatUserFriend::getFriendId,uid)
                    .set(ImChatUserFriend::getFriendStatus,ImConfigConst.FRIEND_STATUS_NOT_VERIFY)
                    .update();
        }
        Users users=commonQuery.getUser(Convert.toInt(friendId));
        ImChatUserFriend friend=new ImChatUserFriend();
        friend.setFriendStatus(ImConfigConst.FRIEND_STATUS_NOT_VERIFY);
        friend.setFriendId(Convert.toInt(friendId));
        friend.setUserId(Convert.toInt(uid));
        friend.setRemark(users.getName());
        friend.setCreateTime(LocalDateTime.now());
        friendService.save(friend);
        redisUtil.set(uid+"_"+friendId,friend,ImConfigConst.REDIS_EXPIRE_TIME);
        //通知申请目标进行审核
        String content=SecurityUtils.getUser().getName()+"申请添加您为好友";
        ImChatUserMessage imChatUserMessage= SysPushMessage.builderMessage(users.getId(),content);
        SysPushMessage.send(imChatUserMessage);
        return Result.result("200","申请添加好友成功，待审核",true);
    }
    //TODO 获取好友列表
    @PostMapping("/list")
    @Operation(summary = "获取列表信息[申请列表:0,好友列表：1]")
    public Result<List<UserFriendVO>> getFriendList(@RequestParam("status") String status){
        Long uid=SecurityUtils.getUserId();
        LambdaQueryChainWrapper<ImChatUserFriend> query=friendService.lambdaQuery();
        if(status.equals("0")){
            query.eq(ImChatUserFriend::getFriendId,uid);
        }else{
            query.eq(ImChatUserFriend::getUserId,uid);
        }
        List<ImChatUserFriend> friendList=query
               .eq(ImChatUserFriend::getFriendStatus,status)
               .list();
        List<UserFriendVO> userFriendVOS=new ArrayList<>(friendList.stream().map(i->
                getUserFriendVO(commonQuery.getUser(i.getFriendId()),i)).toList());
        if(status.equals("0")){
            userFriendVOS.removeIf(f->redisUtil.hasKey(f.getId().toString()));
        }
        return Result.success(userFriendVOS);
    }
    //TODO 获取好友详情
    @PostMapping("/detail")
    @Operation(summary = "获取好友详情[前端vo对象中的id]")
    public Result<UserFriendVO> getFriendDetail(@RequestParam("id") String id){
        ImChatUserFriend imChatUserFriend=friendService.lambdaQuery().eq(ImChatUserFriend::getId,id).one();
        if(imChatUserFriend!=null){
            return Result.success(getUserFriendVO(commonQuery.getUser(imChatUserFriend.getFriendId()),imChatUserFriend));
        }
        return Result.failed("不存在该好友");
    }

    //TODO 审核好友申请
    @PostMapping("/apply_check")
    @Operation(summary = "审核好友申请[isPass:1 通过，-1 不通过]")
    public Result<Boolean> checkFriendApply(@RequestParam("id") String id, @RequestParam("isPass")String isPass, @RequestParam("remark") String remark){
        ImChatUserFriend friend=friendService.lambdaQuery().eq(ImChatUserFriend::getId,id).one();
        if(!redisUtil.hasKey(friend.getUserId()+"_"+friend.getFriendId())){
            friendService.removeById(id);
            return Result.failed("申请已经过期");
        }
        boolean result=friendService.lambdaUpdate().set(ImChatUserFriend::getFriendStatus,isPass)
                .eq(ImChatUserFriend::getId,id).update();

        ImChatUserFriend chatUserFriend=new ImChatUserFriend();
        chatUserFriend.setFriendStatus(ImConfigConst.FRIEND_STATUS_PASS);
        chatUserFriend.setFriendId(friend.getUserId());
        chatUserFriend.setUserId(friend.getFriendId());
        chatUserFriend.setRemark(remark);
        chatUserFriend.setCreateTime(LocalDateTime.now());
        boolean result1=friendService.save(chatUserFriend);

        String content=SecurityUtils.getUser().getName()+"通过了您的好友申请，你可以去跟他打招呼了";
        ImChatUserMessage imChatUserMessage= SysPushMessage.builderMessage(friend.getUserId(),content);
        SysPushMessage.send(imChatUserMessage);
        return Result.result("200",result && result1?"审核通过":"操作异常，请稍后重试！",result && result1);
    }
    //TODO 修改好友备注
    @PostMapping("/remark")
    @Operation(summary = "修改好友备注")
    public Result<Boolean> modifyFriendRemark(@RequestParam("id") String id, @RequestParam("remark")String remark){
        if(!remark.isEmpty()){
            boolean result= friendService.lambdaUpdate().set(ImChatUserFriend::getRemark,remark)
                    .eq(ImChatUserFriend::getId,id)
                    .update();
            return Result.result("200",result?"审核通过":"操作异常，请稍后重试！",result);
        }
        return Result.failed("备注不能为空");
    }

    public UserFriendVO getUserFriendVO(Users users,ImChatUserFriend imChatUserFriend){
        UserFriendVO userFriend = new UserFriendVO();
        userFriend.setUserId(imChatUserFriend.getUserId());
        userFriend.setUsername(users.getName());
        userFriend.setAvatar(users.getAvatar());
        userFriend.setFriendStatus(imChatUserFriend.getFriendStatus());
        userFriend.setCreateTime(imChatUserFriend.getCreateTime());
        userFriend.setRemark(imChatUserFriend.getRemark());
        userFriend.setFriendId(imChatUserFriend.getFriendId());
        userFriend.setId(imChatUserFriend.getId());
        return userFriend;
    }
}
