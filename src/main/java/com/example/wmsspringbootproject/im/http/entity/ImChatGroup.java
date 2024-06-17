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
 * 聊天群
 * </p>
 *
 * @author 初秋
 * @since 2024-06-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_chat_group")
@Schema(description="聊天群实体类")
public class ImChatGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description="群id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description="群名称")
    @TableField("group_name")
    private String groupName;

    @Schema(description="群主id")
    @TableField("master_user_id")
    private Integer masterUserId;

    @Schema(description="类型[1:聊天群，2:话题]")
    @TableField("group_type")
    private Integer groupType;

    @Schema(description="群头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description="群简介")
    @TableField("introduction")
    private String introduction;

    @Schema(description="群公告")
    @TableField("notice")
    private String notice;

    @Schema(description="进入方式[0:无需验证，1:需要群主或管理员同意]")
    @TableField("in_type")
    private Boolean inType;

    @Schema(description="群创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
