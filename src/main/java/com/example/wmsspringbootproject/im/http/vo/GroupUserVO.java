package com.example.wmsspringbootproject.im.http.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * TODO 群成员 视图对象
 * @author 初秋
 * @since 2024-06-09
 */
@Data
public class GroupUserVO {

    @Schema(description="id")
    private Integer id;

    @Schema(description="群id")
    private Integer groupId;

    @Schema(description="用户id")
    private Integer userId;

    @Schema(description="用户名")
    private String username;

    @Schema(description="用户头像")
    private String avatar;

    @Schema(description="审核用户id")
    private Integer verifyUserId;

    @Schema(description="是否是管理员 是否管理员[0:否，1:是]")
    private Boolean adminFlag;

    @Schema(description="备注")
    private String remark;

    @Schema(description="用户状态 用户状态[0:未审核，1:审核通过，2:禁言]")
    private Integer userStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description="创建时间")
    private LocalDateTime createTime;

    @Schema(description="群名称")
    private String groupName;
}
