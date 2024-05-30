package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Warehouses {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String address;
    private String contactPerson;
    private String contactPhone;
    private String createTime;
    private int status;
    private int deleted;
    // 省略其他属性和方法
}
