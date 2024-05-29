package com.example.wmsspringbootproject.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class BasePageQuery {

    @Schema(description = "页码", required = true, example = "1")
    private int pageNum = 1;

    @Schema(description = "每页记录数", required = true, example = "10")
    private int pageSize = 10;
}
