package com.example.wmsspringbootproject.model.query;

import com.example.wmsspringbootproject.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description ="角色查询对象")
@Data
public class SysRoleQuery extends BasePageQuery {
    @Schema(description="关键字(角色名)")
    private String keyword;
}
