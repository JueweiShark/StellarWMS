package com.example.wmsspringbootproject.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TransactionProduct {
    private Long id;
    private String name;
    private int typeId;
    private String picture;
    private int number;
    private String util;
    @TableField("transaction_id")
    private Long transactionId;
}
