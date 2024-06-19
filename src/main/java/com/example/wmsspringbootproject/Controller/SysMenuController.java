package com.example.wmsspringbootproject.Controller;

import com.example.wmsspringbootproject.Service.SysMenuService;
import com.example.wmsspringbootproject.common.model.Option;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.form.MenuForm;
import com.example.wmsspringbootproject.model.query.MenuQuery;
import com.example.wmsspringbootproject.model.vo.MenuVO;
import com.example.wmsspringbootproject.model.vo.RouteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "03.菜单管理")
@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class SysMenuController {

    private final SysMenuService menuService;

    @Operation(summary = "获取菜单列表")
    @GetMapping
    public Result<List<MenuVO>> listMenus(@ParameterObject MenuQuery queryParams) {
        List<MenuVO> menuList = menuService.listMenus(queryParams);
        return Result.success(menuList);
    }

    @Operation(summary = "菜单下拉列表")
    @GetMapping("/options")
    public Result<List<Option<Long>>> listMenuOptions() {
        List<Option<Long>> menus = menuService.listMenuOptions();
        return Result.success(menus);
    }

    @Operation(summary = "路由列表")
    @GetMapping("/routes")
    public Result<List<RouteVO>> listRoutes() {
        List<RouteVO> routeList = menuService.listRoutes();
        return Result.success(routeList);
    }

    @Operation(summary = "菜单表单数据")
    @GetMapping("/{id}/form")
    public Result<MenuForm> getMenuForm(
            @Parameter(description =  "菜单ID") @PathVariable Long id
    ) {
        MenuForm menu = menuService.getMenuForm(id);
        return Result.success(menu);
    }

    @Operation(summary = "新增菜单")
    @PostMapping("/add")
    @PreAuthorize("@bt.hasPerm('sys:menu:add')")
    public Result addMenu(@RequestBody MenuForm menuForm) {
        boolean result = menuService.saveMenu(menuForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改菜单")
    @PutMapping(value = "/modify")
    @PreAuthorize("@bt.hasPerm('sys:menu:edit')")
    public Result updateMenu(
            @RequestBody MenuForm menuForm
    ) {
        boolean result = menuService.saveMenu(menuForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@bt.hasPerm('sys:menu:delete')")
    public Result deleteMenu(
            @Parameter(description ="菜单ID，多个以英文(,)分割") @PathVariable("id") Long id
    ) {
        boolean result = menuService.deleteMenu(id);
        return Result.judge(result);
    }

    @Operation(summary = "修改菜单显示状态")
    @PatchMapping("/status/{menuId}")
    public Result updateMenuVisible(
            @Parameter(description =  "菜单ID") @PathVariable Long menuId,
            @Parameter(description =  "显示状态(1:显示;0:隐藏)") Integer visible

    ) {
        boolean result =menuService.updateMenuVisible(menuId, visible);
        return Result.judge(result);
    }
}

