package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ProductBreakage {
    @TableId(type= IdType.AUTO)
    private Long id;
    private Long productId;
    private Long warehouseId;
    private int number;
    private String util;
    private String createTime;
}
