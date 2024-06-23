package com.example.wmsspringbootproject.im.http.controller;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.wmsspringbootproject.Utils.*;
import com.example.wmsspringbootproject.common.Annotation.LogNote;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserFriend;
import com.example.wmsspringbootproject.im.http.entity.ImChatUserMessage;
import com.example.wmsspringbootproject.im.http.service.ImChatUserFriendService;
import com.example.wmsspringbootproject.im.http.vo.UserFriendVO;
import com.example.wmsspringbootproject.im.websocket.MessageCache;
import com.example.wmsspringbootproject.im.websocket.constants.ImConfigConst;
import com.example.wmsspringbootproject.model.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tio.websocket.common.WsResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    MessageCache messageCache;

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

    //TODO 记录聊天对象 //twainId 单聊 加前缀solo_ 群聊group_
    @GetMapping("/record")
    @LogNote(description="记录聊天对象")
    @Operation(summary = "记录聊天对象")
    public Result<Boolean> recordFriend(@RequestParam("twainId")String twainId){
        Long uid=SecurityUtils.getUserId();
        if(TextUtil.textIsEmpty(WmsCache.getTwainMap().get(Convert.toInt(uid)))){
            WmsCache.getTwainMap().put(Convert.toInt(uid),twainId);
        }
        if(!WmsCache.getTwainMap().get(Convert.toInt(uid)).equals(twainId)){
            WmsCache.getTwainMap().replace(Convert.toInt(uid),twainId);
        }
        return Result.success();
    }


    //TODO 添加好友
    @PostMapping("/add")
    @LogNote(description="添加好友")
    @Operation(summary = "添加好友")
    public Result<Boolean> addFriend(@RequestParam("friendId")String friendId){
        Long uid=SecurityUtils.getUserId();
        List<Integer> statusList=new ArrayList<>();
        statusList.add(ImConfigConst.FRIEND_STATUS_BAN);
        statusList.add(ImConfigConst.FRIEND_STATUS_NOT_VERIFY);
        ImChatUserFriend chatUserFriend=friendService.lambdaQuery()
                .eq(ImChatUserFriend::getFriendId,friendId)
                .eq(ImChatUserFriend::getUserId,uid)
                .in(ImChatUserFriend::getFriendStatus,statusList).one();
        if(chatUserFriend!=null&&chatUserFriend.getFriendStatus()==ImConfigConst.FRIEND_STATUS_BAN){
            friendService.lambdaUpdate().eq(ImChatUserFriend::getUserId,uid)
                    .eq(ImChatUserFriend::getFriendId,friendId)
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
        messageCache.putUserMessage(imChatUserMessage);
        SysPushMessage.send(imChatUserMessage);
        return Result.result("200","申请添加好友成功，待审核",true);
    }
    //TODO 获取好友列表
    @GetMapping("/list")
    @Operation(summary = "获取列表信息[申请列表:0,好友列表：1]")
    public Result<List<UserFriendVO>> getFriendList(@RequestParam("status") String status){
        Long uid=SecurityUtils.getUserId();
        LambdaQueryChainWrapper<ImChatUserFriend> query=friendService.lambdaQuery();
        query.eq(ImChatUserFriend::getFriendStatus,Convert.toInt(status));
        if(status.equals("0")){
            query.and(select->
                            select.eq(ImChatUserFriend::getFriendId,uid)
                                    .or(item->item.eq(ImChatUserFriend::getUserId,uid)));
        }else{
            query.eq(ImChatUserFriend::getUserId,uid);
        }
        List<ImChatUserFriend> friendList=query
               .list();
        if(!friendList.isEmpty()){
            List<UserFriendVO> userFriendVOS=new ArrayList<>(friendList.stream().map(i->
                    getUserFriendVO(commonQuery.getUser(status.equals("0")?
                            Objects.equals(i.getUserId(),Convert.toInt(uid))?i.getFriendId():i.getUserId()
                            :i.getFriendId()),i)).toList());
            ImChatUserFriend imChatUserFriend=redisUtil.get(userFriendVOS.get(0).getUserId()+"_"+userFriendVOS.get(0).getFriendId());
            if(status.equals("0")){
                userFriendVOS.removeIf(f->!redisUtil.hasKey(f.getUserId()+"_"+f.getFriendId()));
            }
            return Result.success(userFriendVOS);
        }else{
            return Result.result("200","暂无好友",new ArrayList<UserFriendVO>());
        }
    }
    //TODO 获取好友详情
    @GetMapping("/detail")
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
        String content="";
        if(isPass.equals("1")){
            content=SecurityUtils.getUser().getName()+"通过了您的好友申请，你可以去跟他打招呼了";
        }else{
            content=SecurityUtils.getUser().getName()+"拒绝了您的好友申请";
        }
        ImChatUserMessage imChatUserMessage= SysPushMessage.builderMessage(friend.getUserId(),content);
        SysPushMessage.send(imChatUserMessage);
        if(isPass.equals("1")){
            ImChatUserFriend chatUserFriend=new ImChatUserFriend();
            chatUserFriend.setFriendStatus(ImConfigConst.FRIEND_STATUS_PASS);
            chatUserFriend.setFriendId(friend.getUserId());
            chatUserFriend.setUserId(friend.getFriendId());
            chatUserFriend.setRemark(remark);
            chatUserFriend.setCreateTime(LocalDateTime.now());
            boolean result1=friendService.save(chatUserFriend);
            return Result.result("200",result && result1?"审核通过":"操作异常，请稍后重试！",result && result1);
        }else{
            return Result.failed("您拒绝该好友申请");
        }
    }
    //TODO 修改好友备注
    @GetMapping("/remark")
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
