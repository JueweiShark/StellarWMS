package com.example.wmsspringbootproject.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.Service.WareHouseService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.form.WareHouseForm;
import com.example.wmsspringbootproject.model.query.WarehouseQuery;
import com.example.wmsspringbootproject.model.vo.WareHouseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "02.仓库管理")
@CrossOrigin

@RequestMapping("/wareHouse")
public class WareHouseController {

    private final WareHouseService service;

    @GetMapping("/Details/{id}")
    @Operation(summary = "获取仓库详情")
    public Result<WareHouseVO> getWareHouse(
            @PathVariable("id") Integer id
    ){
        return service.getWareHouseDetails(id);
    }

    @PostMapping("/add")
    @Operation(summary = "添加仓库")
    public Result<Boolean> addWareHouse(
            @RequestBody WareHouseForm wareHouseForm
    ){
        return service.saveWareHouseInfo(wareHouseForm);
    }

    @DeleteMapping("/delete/{ids}'")
    @Operation(summary = "删除仓库信息")
    public Result<Boolean> deleteWareHouse(
           @Parameter(description="需要删除的ids多个id用','隔开") @PathVariable("ids") String ids
    ){
        return service.removeWareHouseInfo(ids);
    }

    @GetMapping("/list")
    @Operation(summary = "获取仓库列表")
    public Result<IPage<WareHouseVO>> listWareHouse(
        @ParameterObject WarehouseQuery query
    ){
        return service.warehouseList(query);
    }

    @PutMapping("/update")
    @Operation(summary = "修改仓库信息")
    public Result<Boolean> updateWareHouse(
            @ParameterObject WareHouseForm wareHouseForm
    ){
        return service.updateWareHouseInfo(wareHouseForm);
    }

}
