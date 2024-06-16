package com.example.wmsspringbootproject.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.Service.ProductService;
import com.example.wmsspringbootproject.Service.ProductTypeService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.form.ProductForm;
import com.example.wmsspringbootproject.model.query.ProductQuery;
import com.example.wmsspringbootproject.model.query.WarehouseQuery;
import com.example.wmsspringbootproject.model.vo.ProductTypeVO;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "03.产品管理")
@CrossOrigin

@RequestMapping("/product")
public class ProductController {

    private final ProductService service;
    private final ProductTypeService productTypeService;

    @GetMapping("/Details/{id}")
    @Operation(summary = "获取产品详情")
    public Result<ProductVO> getProduct(
            @PathVariable("id") Integer id
    ){
        return service.getProductDetails(id);
    }

    @PostMapping("/add")
    @Operation(summary = "添加产品")
    public Result<Boolean> addProduct(
            @RequestBody ProductForm productForm
    ){
        return service.saveProductInfo(productForm);
    }

    @DeleteMapping("/delete/{ids}")
    @Operation(summary = "删除产品信息")
    public Result<Boolean> deleteProduct(
           @Parameter(description="需要删除的ids多个id用','隔开") @PathVariable("ids") String ids
    ){
        return service.removeProductInfo(ids);
    }

    @GetMapping("/list")
    @Operation(summary = "获取产品列表")
    public Result<IPage<ProductVO>> listProduct(
        @ParameterObject ProductQuery query
    ){
        return service.productList(query);
    }

    @PutMapping("/update")
    @Operation(summary = "修改产品信息")
    public Result<Boolean> updateProduct(
            @RequestBody ProductForm productForm
    ){
        return service.updateProductInfo(productForm);
    }
    @GetMapping("/typeList")
    @Operation(summary = "获取产品类别列表")
    public Result<List<ProductTypeVO>> getProduct(){
        return productTypeService.productTypeList();
    }

}
