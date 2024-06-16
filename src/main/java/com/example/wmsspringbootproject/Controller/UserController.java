package com.example.wmsspringbootproject.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.Service.AuthUserService;
import com.example.wmsspringbootproject.Service.SysRoleService;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.model.entity.SysRole;
import com.example.wmsspringbootproject.model.entity.UserType;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.query.UserQuery;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "01.用户管理")
@RestController
@RequiredArgsConstructor
@CrossOrigin

@Transactional
public class UserController {
    private final UserService userService;
    private final AuthUserService authUserService;
    private final SysRoleService sysRoleService;
    @Operation(summary = "获取用户列表")
    @GetMapping("/getInfo")
    public Result<IPage<UserVO>> UserList(
            @ParameterObject UserQuery userQuery
    ) {
        return userService.UserList(userQuery);
    }
    @Operation(summary = "新增用户")
    @PostMapping("/addUser")
    public Result<Boolean> addUser(
            @Valid @RequestBody UserForm formData
    ) {
       return userService.addUser(formData);
    }
    @Operation(summary = "修改用户")
    @PutMapping("updateUser")
    public Result<Boolean> updateUser(
            @Valid @RequestBody UserForm formData
    ) {
        return userService.updateUser(formData);
    }
    @Operation(summary = "重置密码")
    @PutMapping("/resetPassword/{id}")
    public Result<Boolean> resetPassWord(
            @Parameter(description ="请输入id") @PathVariable("id") int id
    ) {
        return userService.resetPassword(id);
    }
    @Operation(summary = "删除用户")
    @DeleteMapping("/deleteUser/{ids}")
    public com.example.wmsspringbootproject.common.result.Result<Boolean> deleteUser(
            @Parameter(description ="请输入id") @PathVariable("ids") String ids
    ) {
        return userService.deleteUser(ids);
    }
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public com.example.wmsspringbootproject.model.vo.Result Login(
            @Valid @RequestBody UserForm formData
    ) {
        return authUserService.Login(formData);
    }
    @Operation(summary = "获取用户类型列表")
    @GetMapping("/getTypeList")
    public Result<List<SysRole>> UserTypeList() {
        return sysRoleService.getUserType();
    }
}
