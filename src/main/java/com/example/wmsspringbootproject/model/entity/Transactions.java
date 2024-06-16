package com.example.wmsspringbootproject.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class Transactions {
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
    private String productIds;
    private Long warehouseId;
    private int deleted;
    private int progress;
}
