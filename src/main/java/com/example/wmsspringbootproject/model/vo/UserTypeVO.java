package com.example.wmsspringbootproject.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description ="用户表单")
@Data
public class UserTypeVO {
    @Schema(description ="类型id")
    private int id;
    @Schema(description ="类型名称")
    private String name;
    @Schema(description ="类型码")
    private String code;
    @Schema(description ="类型分类")
    private int sort;
    @Schema(description ="类型状态")
    private int status;
    @Schema(description ="是否已被删除")
    private int deleted;
    @Schema(description ="数据权限")
    private int dataScope;
}
