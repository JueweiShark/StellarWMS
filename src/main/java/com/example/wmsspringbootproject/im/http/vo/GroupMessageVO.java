package com.example.wmsspringbootproject.im.http.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * TODO 群消息 视图对象
 * @author 初秋
 * @since 2024-06-09
 */
@Data
public class GroupMessageVO {

    private Long id;

    @Schema(description="群id")
    private Integer groupId;

    @Schema(description="发送方id")
    private Integer fromId;

    @Schema(description="接收方id")
    private Integer toId;

    @Schema(description="发送内容")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description="发送时间")
    private LocalDateTime createTime;

    @Schema(description="头像")
    private String avatar;

    @Schema(description="用户名")
    private String username;
}
