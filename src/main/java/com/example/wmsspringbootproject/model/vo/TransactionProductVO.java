package com.example.wmsspringbootproject.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TransactionProductVO {
    @Schema(description = "事务产品ID")
    private String id;
    @Schema(description = "事务产品名称")
    private String name;
    @Schema(description = "事务产品数量")
    private int number;
    @Schema(description = "事务单位ID")
    private String util;
    @TableField("transaction_id")
    private String transactionId;
}
