package com.example.wmsspringbootproject.model.query;

import com.example.wmsspringbootproject.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description ="产品查询对象")
@Data
public class ProductQuery extends BasePageQuery {
    @Schema(description="产品名称")
    private String name;
    @Schema(description="产品类型")
    private Integer typeId;
    @Schema(description="产品所属仓库")
    private Integer warehouseId;
    @Schema(description="产品入库时间")
    private String createTime;
}
