package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Users {
    @TableId(type= IdType.AUTO)
    private int id;
    private String name;
    private String nickName;
    private String password;
    private int type;
    private String avatar;
    private String email;
    private String phone;
    private String weChatName;
    private String createTime;
    private String lastLogin;
    private int deleted;

}
