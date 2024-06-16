package com.example.wmsspringbootproject.im.http.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * TODO 用户好友 视图对象
 * @author 初秋
 * @since 2024-06-09
 */
@Data
public class UserFriendVO {

    @Schema(description="id")
    private Integer id;

    @Schema(description="用户id")
    private Integer userId;

    @Schema(description="朋友id")
    private Integer friendId;

    @Schema(description="用户名")
    private String username;

    @Schema(description="头像")
    private String avatar;

    @Schema(description="朋友状态[0:未审核，1:审核通过]")
    private Integer friendStatus;

    @Schema(description="备注")
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
