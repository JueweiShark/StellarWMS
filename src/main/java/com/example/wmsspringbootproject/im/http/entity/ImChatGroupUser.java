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
 * 聊天群成员
 * </p>
 *
 * @author 初秋
 * @since 2024-06-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_chat_group_user")
public class ImChatGroupUser implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(description = "群id")
    @TableField("group_id")
    private Integer groupId;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Integer userId;


    @Schema(description = "审核用户id")
    @TableField("verify_user_id")
    private Integer verifyUserId;

    @Schema(description = "是否管理员[0:否，1:是]")
    @TableField("admin_flag")
    private Boolean adminFlag;


    @Schema(description="备注")
    @TableField("remark")
    private String remark;


    @Schema(description="用户状态[0:未审核，1:审核通过，2:禁言]")
    @TableField("user_status")
    private Integer userStatus;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

}
