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
 * 群聊记录
 * </p>
 *
 * @author 初秋
 * @since 2024-06-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_chat_user_group_message")
public class ImChatUserGroupMessage implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(description="群聊记录id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="群id")
    @TableField("group_id")
    private Integer groupId;

    @Schema(description="发送方id")
    @TableField("from_id")
    private Integer fromId;

    @Schema(description="接收方id")
    @TableField("to_id")
    private String toId;

    @Schema(description="发送内容")
    @TableField("content")
    private String content;

    @Schema(description = "是否已读[0:未读，1:已读]")
    @TableField("message_status")
    private Boolean messageStatus;

    @Schema(description="创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

}
