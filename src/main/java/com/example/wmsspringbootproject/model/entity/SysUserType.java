package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "系统用户角色表")
public class SysUserType {
    private Integer userId;
    private Integer roleId;
}
