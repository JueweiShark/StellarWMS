package com.example.wmsspringbootproject.im.websocket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 消息类
 * </p>
 *
 * @author 初秋
 * @since 2024-06-10
 */
@Data
public class ImMessage {

    @Schema(description = "消息类型")
    private Integer messageType;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "发送方id")
    private Integer fromId;

    @Schema(description = "接收方id")
    private Integer toId;

    @Schema(description = "群id")
    private Integer groupId;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户名")
    private String username;
}
