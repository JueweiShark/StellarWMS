package com.example.wmsspringbootproject.Controller;

import com.example.wmsspringbootproject.Service.ProductBreakageService;
import com.example.wmsspringbootproject.model.form.ProductBreakageForm;
import com.example.wmsspringbootproject.model.query.ProductBreakageQuery;
import com.example.wmsspringbootproject.model.vo.ProductBreakageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import com.example.wmsspringbootproject.common.result.Result;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "10.报废管理")
@CrossOrigin

@RequestMapping("/productBreakage")
public class ProductBreakageController {

    private final ProductBreakageService productBreakageService;

    @PostMapping("/add")
    @Operation(summary = "提交报废列表")
    public Result<Boolean> addProductBreakage(
            @RequestBody ProductBreakageQuery query
            ){
        System.out.println(query.getProductBreakageForms().get(0));
        return productBreakageService.addProductBreakage(query);
    }
    @GetMapping("/list")
    @Operation(summary = "获取报废列表")
    public Result<List<ProductBreakageVO>> listProductBreakage(
        @ParameterObject ProductBreakageForm form
    ){
        return productBreakageService.productBreakageList(form);
    }
}
