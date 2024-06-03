package com.example.wmsspringbootproject.Controller;

import com.example.wmsspringbootproject.Service.AuthUserService;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.model.dto.LoginResult;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.query.UserQuery;
import com.example.wmsspringbootproject.model.vo.Result;
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
    @Operation(summary = "获取用户列表")
    @GetMapping("/getInfo")
    public List<Users> UserList(
            @ParameterObject UserQuery userQuery
    ) {
        return userService.UserList(userQuery);
    }
    @Operation(summary = "新增用户")
    @PostMapping("/addUser")
    public Result addUser(
            @Valid @RequestBody UserForm formData
    ) {
       return userService.addUser(formData);
    }
    @Operation(summary = "修改用户")
    @PutMapping("updateUser")
    public Boolean updateUser(
            @Valid @RequestBody UserForm formData
    ) {
        return userService.updateUser(formData);
    }
    @Operation(summary = "删除用户")
    @DeleteMapping("/deleteUser/{id}")
    public Boolean deleteUser(
            @Parameter(description ="请输入id") @PathVariable("id") Integer id
    ) {
        return userService.deleteUser(id);
    }
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result Login(
            @Valid @RequestBody UserForm formData
    ) {
        return authUserService.Login(formData);
    }
}
