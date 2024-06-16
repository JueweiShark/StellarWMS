package com.example.wmsspringbootproject.model.form;

import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TransactionsForm {
    @Schema(description = "事务ID")
    private Long id;
    @Schema(description = "事务标题")
    private String title;
    @Schema(description = "事务描述")
    private String description;
    @Schema(description = "事务类型")
    private String transactionType;
    @Schema(description = "创建人ID")
    private long creatorId;
    @Schema(description = "确认人ID")
    private long confirmatorId;
    @Schema(description = "审核人ID")
    private long auditorId;
    @Schema(description = "创建时间")
    private String createTime;
    @Schema(description = "更新时间")
    private String updateTime;
    @Schema(description = "事务开始时间")
    private String startTime;
    @Schema(description = "事务截至时间")
    private String endTime;
    @Schema(description = "事务状态")
    private String status;
    @Schema(description = "产品列表")
    private List<TransactionProduct> productList;
    @Schema(description = "仓库ID")
    private Long warehouseId;
    private int deleted;
    @Schema(description = "进度")
    private int progress;
}
