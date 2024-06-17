package com.example.wmsspringbootproject.model.query;

import com.example.wmsspringbootproject.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WarehouseQuery extends BasePageQuery {
    private Long id;
    private String name;
    private String address;
    private String contactPerson;
    private String contactPhone;
    private String createTime;
    private int status;
    private int deleted;
}
