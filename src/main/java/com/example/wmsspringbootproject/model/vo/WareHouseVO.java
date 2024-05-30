package com.example.wmsspringbootproject.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "仓库视图对象")
@Data
public class WareHouseVO {
    @Schema(description = "仓库编号")
    private Integer id;
    @Schema(description = "仓库名称")
    private String name;
    @Schema(description = "仓库地址")
    private String address;
    @Schema(description = "仓库合同签字人")
    private String contactPerson;
    @Schema(description = "仓库合同手机号")
    private String contactPhone;
    @Schema(description = "仓库创建时间")
    private String createTime;
    @Schema(description = "仓库状态")
    private int status;
    @Schema(description = "仓库是否被删除")
    private int deleted;
}
