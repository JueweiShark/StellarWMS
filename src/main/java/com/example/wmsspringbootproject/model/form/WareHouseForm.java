package com.example.wmsspringbootproject.model.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "仓库表单对象")
@Data
public class WareHouseForm{
    @Schema(description = "仓库编号")
    private Integer id;

    @NotNull(message = "仓库名称不能为空")
    @Schema(description = "仓库名称")
    private String name;

    @NotNull(message = "仓库地址不能为空")
    @Schema(description = "仓库地址")
    private String address;

    @NotNull(message = "仓库合同签字人不能为空")
    @Schema(description = "仓库合同签字人")
    private String contactPerson;

    @NotNull(message = "仓库合同手机号不能为空")
    @Schema(description = "仓库合同手机号")
    private String contactPhone;

    @Schema(description = "仓库创建时间")
    private String createTime;
    @Schema(description = "仓库状态")
    private int status;
    @Schema(description = "仓库是否被删除")
    private int deleted;
}
