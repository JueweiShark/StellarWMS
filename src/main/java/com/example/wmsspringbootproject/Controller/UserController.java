package com.example.wmsspringbootproject.Controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.entity.Warehouse;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.query.UserQuery;
import com.example.wmsspringbootproject.model.query.WarehouseQuery;
import com.example.wmsspringbootproject.model.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "01.用户管理")
@RestController
@RequiredArgsConstructor
@CrossOrigin

@Transactional
public class UserController {
    private final UserService userService;

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
            @Parameter(description = "请输入id") @PathVariable("id") Integer id
    ) {
        return userService.deleteUser(id);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result Login(
            @Valid @RequestBody UserForm formData
    ) {
        return userService.Login(formData);
    }


    @Operation(summary = "仓库列表")
    @GetMapping("/warehouseList")
    public Result warehouseList(
            @ParameterObject WarehouseQuery warehouseQueryParam
    ) {
        System.out.println(warehouseQueryParam.getPageNum());
        System.out.println(warehouseQueryParam.getName());
        Page<Warehouse> warehousePage = new Page<>(warehouseQueryParam.getPageNum(), 10);
        List<Warehouse> list = new ArrayList<>();
            for (int j = 0; j < warehouseQueryParam.getPageSize(); j++) {
                Warehouse warehouse = new Warehouse();
                warehouse.setId(((warehouseQueryParam.getPageNum() - 1) * 10 + j + 1));
                if (warehouseQueryParam.getName() != null) {
                    warehouse.setName(warehouseQueryParam.getName() + j);
                }else {
                    warehouse.setName("仓库" + j);
                }
                if (warehouseQueryParam.getAddress() != null) {
                    warehouse.setAddress(warehouseQueryParam.getAddress() + j);
                }else {
                    warehouse.setAddress("地址" + j);
                }
                if (warehouseQueryParam.getContactPerson() != null) {
                    warehouse.setContactPerson(warehouseQueryParam.getContactPerson() + j);
                }else {
                    warehouse.setContactPerson("联系人" + j);
                }
                warehouse.setContactPhone("电话" + j);
                warehouse.setStatus(warehouse.getId()%2);
                LocalDate now = LocalDate.now();
                warehouse.setCreateTime(String.valueOf(now));
                list.add(warehouse);
            }
        warehousePage.setTotal(1000);
        warehousePage.setRecords(list);
        return Result.success(warehousePage);
    }

}
