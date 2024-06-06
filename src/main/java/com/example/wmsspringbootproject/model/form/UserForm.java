package com.example.wmsspringbootproject.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Schema(description ="用户表单")
@Data
@AllArgsConstructor
public class UserForm implements Serializable {
    @Schema(description ="用户id")
    private int id;
    @Schema(description ="用户名称")
    private String name;
    @Schema(description ="用户昵称")
    private String nickName;
    @Schema(description ="用户密码")
    private String password;
    @Schema(description ="用户头像")
    private String avatar;
    @Schema(description ="用户电子邮件")
    private String email;
    @Schema(description ="用户手机号")
    private String phone;
    @Schema(description ="用户表微信名")
    private String weChatName;
    @Schema(description ="用户注册时间")
    private String createTime;
    @Schema(description ="用户上次登录时间")
    private String lastLogin;
    @Schema(description ="是否已被删除")
    private int deleted;
    @Schema(description ="邮箱登录：验证码")
    private String code;
}
