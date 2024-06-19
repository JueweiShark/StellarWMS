package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.ProductService;
import com.example.wmsspringbootproject.Service.ProductTypeService;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.common.Annotation.Subject;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.converter.ProductConverter;
import com.example.wmsspringbootproject.mapper.ProductMapper;
import com.example.wmsspringbootproject.mapper.ProductTypeMapper;
import com.example.wmsspringbootproject.mapper.WareHouseMapper;
import com.example.wmsspringbootproject.model.entity.Products;
import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.form.ProductForm;
import com.example.wmsspringbootproject.model.query.ProductQuery;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Products> implements ProductService {

    private final ProductConverter converter;
    @Autowired
    private WareHouseMapper wareHouseMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;
//    private final ProductTypeService productTypeService;
    @Override
    public Result<IPage<ProductVO>> productList(ProductQuery query) {
        LambdaQueryWrapper<Products> queryWrapper=new LambdaQueryWrapper<>();
        Page<Products> productPage=new Page<>(query.getPageNum(),query.getPageSize());
        if (query != null) {
            queryWrapper.gt(Products::getId,0);

            if (!TextUtil.textIsEmpty(query.getName())) {
                queryWrapper.and(wrapper -> wrapper.like(Products::getName, query.getName()));
            }
            if (query.getTypeId()!=0) {
                queryWrapper.and(wrapper -> wrapper.like(Products::getTypeId, query.getTypeId()));
            }
            if (query.getWarehouseId()!=0) {
                queryWrapper.and(wrapper -> wrapper.eq(Products::getWarehouseId, query.getWarehouseId()));
            }
            if (!TextUtil.textIsEmpty(query.getCreateTime())) {
                queryWrapper.and(wrapper -> wrapper.like(Products::getCreateTime, query.getCreateTime()));
            }
            if (TextUtil.isNotEmpty(query.getDeleted())){
                queryWrapper.eq(Products::getDeleted,query.getDeleted());
            }else {
                queryWrapper.and(wrapper -> wrapper.eq(Products::getDeleted, 1));
            }
        }
        IPage<Products> productList =this.page(productPage,queryWrapper);
        IPage<ProductVO> productVOIPage = new Page<>();
        productVOIPage.setRecords(productList.getRecords().stream().map(product -> {
            ProductVO productVO = converter.entity2Vo(product);
            productVO.setWarehouses(wareHouseMapper.selectById(productVO.getWarehouseId()));
            productVO.setProductTypes(productTypeMapper.selectById(productVO.getTypeId()));
            productVO.setWarehouseName(productVO.getWarehouses().getName());
            productVO.setTypeName(productVO.getProductTypes().getName());

            return productVO;
        }).toList());
        productVOIPage.setPages(productList.getPages());
        productVOIPage.setCurrent(productList.getCurrent());
        productVOIPage.setSize(productList.getSize());
        productVOIPage.setTotal(productList.getTotal());
        return Result.success(productVOIPage);
    }

    @Subject(filedName = "warehouseId",observer = WareHouseServiceImpl.class)
    @Override
    public Result<Boolean> saveProductInfo(ProductForm form) {
        if (!form.getTypeId().matches("\\d+")){
            LambdaQueryWrapper<ProductTypes> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProductTypes::getName, form.getTypeId());
            List<ProductTypes> products = productTypeMapper.selectList(queryWrapper);
            if (products == null || products.isEmpty()) {
                ProductTypes productType = new ProductTypes();
                productType.setName(form.getTypeId());
                productTypeMapper.insert(productType);
                form.setTypeId(String.valueOf(productType.getId()));
            }
            else {
                form.setTypeId(String.valueOf(products.get(0).getId()));
            }
        }
        Products products =converter.form2Entity(form);

        products.setCreateTime(TextUtil.formatDate(new Date()));
        products.setDeleted(Constants.Default.DELETED);
        Boolean result=this.save(products);
        return Result.success(result);
    }

    @Override
    public Result<Boolean> updateProductInfo(ProductForm form) {
        Products products =this.getById(form.getId());
        if(products !=null){
            Products target=converter.form2Entity(form);
            target.setDeleted(TextUtil.textIsEmpty(form.getDeleted()) ? products.getDeleted() : form.getDeleted());
            target.setCreateTime(TextUtil.textIsEmpty(form.getCreateTime()) ? products.getCreateTime() : form.getCreateTime());
            Boolean result=this.baseMapper.updateById(target)>0;
            return result ? Result.success(result) : Result.failed("更新产品信息失败");
        }
        return Result.failed("产品信息不存在");
    }

    @Override
    public Result<Boolean> removeProductInfo(String ids) {
        String[] idArray=ids.split(",");
        if(idArray.length>1){
            for (String id : idArray) {
                Products products =this.getById(id);
                products.setDeleted(2);
                this.baseMapper.updateById(products);
            }
            return Result.success();
        }else{
            Products products =this.getById(ids);
            products.setDeleted(2);
            Boolean result=this.baseMapper.updateById(products)>0;
            return result ? Result.success(result) : Result.failed("删除产品信息失败");
        }
    }

    @Override
    public Result<ProductVO> getProductDetails(Integer id) {
        Products products =this.getById(id);
        return products ==null ? Result.failed("没有该产品") : Result.success(converter.entity2Vo(products));
    }
}
