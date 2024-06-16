package com.example.wmsspringbootproject.im.http.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * TODO 单聊消息 视图对象
 * @author 初秋
 * @since 2024-06-09
 */
@Data
public class UserMessageVO {

    @Schema(description="id")
    private Long id;

    @Schema(description="发送方id")
    private Integer fromId;

    @Schema(description="接受方id")
    private String toId;

    @Schema(description="消息内容")
    private String content;

    @Schema(description="消息状态 是否已读[0:未读，1:已读]")
    private Boolean messageStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description="创建时间")
    private LocalDateTime createTime;

    @Schema(description="头像")
    private String avatar;
}
