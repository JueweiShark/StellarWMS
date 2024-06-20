package com.example.wmsspringbootproject.model.form;


import com.example.wmsspringbootproject.common.enums.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "角色菜单表单对象")
@Data
public class RoleMenuForm {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单列表")
    private List<Long> menuIds;


}
