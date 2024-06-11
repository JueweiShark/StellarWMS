package com.example.wmsspringbootproject.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description ="产品表单")
@Data
public class ProductForm {
    @Schema(description ="产品id")
    private int id;
    @Schema(description ="产品名称")
    private String name;
    @Schema(description ="产品类型id")
    private int typeId;
    @Schema(description ="产品图片")
    private String picture;
    @Schema(description ="产品入库时间")
    private String createTime;
    @Schema(description ="产品数量")
    private Long number;
    @Schema(description ="单位")
    private String unit;
    @Schema(description ="仓库id")
    private String warehouseId;
    @Schema(description ="是否已被删除")
    private int deleted;
}
