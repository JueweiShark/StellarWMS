package com.example.wmsspringbootproject.model.vo;

import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.entity.Warehouses;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description ="产品类型表单")
@Data

public class ProductTypeVO {
    @Schema(description ="产品id")
    private int id;
    @Schema(description ="产品名称")
    private String name;

    private int count;
}
