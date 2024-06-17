package com.example.wmsspringbootproject.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.Service.AuthUserService;
import com.example.wmsspringbootproject.Service.SysRoleService;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.dto.LoginResult;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.form.UserTypeForm;
import com.example.wmsspringbootproject.model.form.WareHouseForm;
import com.example.wmsspringbootproject.model.query.SysRoleQuery;
import com.example.wmsspringbootproject.model.query.UserQuery;
import com.example.wmsspringbootproject.model.query.WarehouseQuery;
import com.example.wmsspringbootproject.model.vo.UserTypeVO;
import com.example.wmsspringbootproject.model.vo.UserVO;
import com.example.wmsspringbootproject.model.vo.WareHouseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Tag(name = "07.角色管理")
@RestController
@RequiredArgsConstructor
@CrossOrigin

@RequestMapping("/sysRole")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Operation(summary = "获取用户类型列表")
    @GetMapping("/getTypeList")
    public Result<IPage<UserTypeVO>> UserTypeList(
            @ParameterObject SysRoleQuery query
    ) {
        return sysRoleService.getUserType(query);
    }
    @GetMapping("/Details/{id}")
    @Operation(summary = "获取角色详情")
    public Result<UserTypeVO> getRoleById(
            @PathVariable("id") Integer id
    ){
        return sysRoleService.getSysRoleByRid(id);
    }

    @PostMapping("/add")
    @Operation(summary = "添加角色")
    public Result<Boolean> addUserType(
            @RequestBody UserTypeForm userTypeForm
    ){
        return sysRoleService.saveSysRole(userTypeForm);
    }

    @DeleteMapping("/delete/{ids}")
    @Operation(summary = "删除角色信息")
    public Result<Boolean> deleteUserType(
            @Parameter(description="需要删除的ids多个id用','隔开") @PathVariable("ids") String ids
    ){
        return sysRoleService.removeSysRole(ids);
    }
    @PutMapping("/update")
    @Operation(summary = "修改角色信息")
    public Result<Boolean> updateUserType(
            @RequestBody UserTypeForm userTypeForm
            ){
        return sysRoleService.updateSysRole(userTypeForm);
    }
}
