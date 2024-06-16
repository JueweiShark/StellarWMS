package com.example.wmsspringbootproject.model.query;

import com.example.wmsspringbootproject.common.base.BasePageQuery;
import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import lombok.Data;

import java.util.List;
@Data
public class TransactionsQuery extends BasePageQuery {
    private Long id;
    private String title;
    private String description;
    private String  transactionType;
    private long creatorId;
    private long confirmatorId;
    private long auditorId;
    private String createTime;
    private String updateTime;
    private String startTime;
    private String endTime;
    private int status;
    private List<TransactionProduct> productList;
    private Long warehouseId;
    private int progress;

}
