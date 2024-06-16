package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Set;

@Data
public class UserType {
    @TableId(type= IdType.AUTO)
    private int id;
    private String name;
    private String code;
    private int sort;
    private int status;
    private int deleted;
    private int dataScope;

}
