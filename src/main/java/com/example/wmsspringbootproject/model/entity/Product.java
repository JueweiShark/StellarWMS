package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Product {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private Integer typeId;
    private String picture;
    private String createTime;
    private Long number;
    private String unit;
    private int warehouseId;
    private int deleted;
}
