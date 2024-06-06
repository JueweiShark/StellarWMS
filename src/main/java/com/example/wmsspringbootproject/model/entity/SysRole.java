package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "user_type")
@Data
@Schema(description = "系统角色表")
public class SysRole {
    private Integer id;
    private String name;
    private String code;
    private Integer sort;
    private Integer status;
    private Integer deleted;
    private Integer dataScope;
}
