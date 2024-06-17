package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Set;

@Data
public class Users implements Serializable {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String nickName;
    private String password;
    private String avatar;
    private String email;
    private String phone;
    private String weChatName;
    private String createTime;
    private String lastLogin;
    private int deleted;
    private int status;
    @TableField(exist = false)
    private Integer dataScope;

    private String warehouseId;

    @TableField(exist = false)
    private Set<String> roles;
    @TableField(exist = false)
    private int typeId;
}
