package com.example.wmsspringbootproject.im.http.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 好友
 * </p>
 *
 * @author 初秋
 * @since 2024-06-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_chat_user_friend")
public class ImChatUserFriend implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(description="id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(description="用户id")
    @TableField("user_id")
    private Integer userId;


    @Schema(description = "好友id")
    @TableField("friend_id")
    private Integer friendId;


    @Schema(description="朋友状态[0:未审核，1:审核通过]")
    @TableField("friend_status")
    private Integer friendStatus;


    @Schema(description="备注")
    @TableField("remark")
    private String remark;


    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}
