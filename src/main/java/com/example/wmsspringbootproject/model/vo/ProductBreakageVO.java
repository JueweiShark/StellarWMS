package com.example.wmsspringbootproject.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductBreakageVO {
    @Schema(description="id")
    private Long id;
    @Schema(description="货品id")
    private Long productId;
    @Schema(description="仓库id")
    private Long warehouseId;
    @Schema(description="报损数量")
    private int number;
    @Schema(description="单位")
    private String util;
    @Schema(description="报损时间")
    private String createTime;
}
