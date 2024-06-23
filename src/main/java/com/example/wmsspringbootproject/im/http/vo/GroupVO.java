package com.example.wmsspringbootproject.im.http.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * TODO 群 视图对象
 * @author 初秋
 * @since 2024-06-09
 */

@Data
public class GroupVO {

    private Integer id;

    @Schema(description="群名称")
    private String groupName;

    @Schema(description="群头像")
    private String avatar;

    @Schema(description="群简介")
    private String introduction;

    @Schema(description = "群主名字")
    private String masterName;

    @Schema(description = "群成员人数")
    private Long memberCount;

    @Schema(description="类型[1:聊天群，2:话题]")
    private Integer groupType;

    @Schema(description="群公告")
    private String notice;

    @Schema(description = "是否为群成员")
    private Boolean isMember;

    @Schema(description="是否群主[0:否，1:是]")
    private Boolean masterFlag;

    @Schema(description="是否管理员[0:否，1:是]")
    private Boolean adminFlag;

    @Schema(description="进入方式[0:无需验证，1:需要群主或管理员同意]")
    private Boolean inType;

    @Schema(description="用户状态[1:审核通过，2:禁言]")
    private Integer userStatus;

    @Schema(description="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
