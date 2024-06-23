package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.ProductTypeService;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.converter.ProductConverter;
import com.example.wmsspringbootproject.converter.ProductTypeConverter;
import com.example.wmsspringbootproject.mapper.ProductMapper;
import com.example.wmsspringbootproject.mapper.ProductTypeMapper;
import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.entity.Products;
import com.example.wmsspringbootproject.model.vo.ProductTypeVO;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductTypes> implements ProductTypeService {
    private final ProductTypeConverter converter;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTypeMapper productTypeMapper;


    @Override
    public Result<List<ProductTypeVO>> productTypeList(){
        LambdaQueryWrapper<ProductTypes> queryWrapper=new LambdaQueryWrapper<>();
        List<ProductTypes> productList =this.baseMapper.selectList(queryWrapper);
        List<ProductTypeVO> productTypeVO=productList.stream().map(converter::entity2Vo).toList();
        return Result.success(productTypeVO);
    };

    public Result<List<ProductTypeVO>> productTypeSum() {
        List<Products> products = productMapper.selectList(new LambdaQueryWrapper<>());

        Map<Integer,ProductTypeVO> hashMap=new HashMap<>();
        List<ProductTypeVO> productTypeVOList=products.stream().map(product->
        {
            if(hashMap.containsKey(product.getTypeId())){
                int count=hashMap.get(product.getTypeId()).getCount();
                hashMap.get(product.getTypeId()).setCount(count+1);
            }else{
                ProductTypeVO productTypeVO = new ProductTypeVO();
                productTypeVO.setId(product.getTypeId());
                productTypeVO.setName(productTypeMapper.selectById(product.getTypeId()).getName());
                productTypeVO.setCount(1);
                hashMap.put(product.getTypeId(),productTypeVO);
            }
            return hashMap.get(product.getTypeId());
        }).toList();
        // 遍历查询结果

        // 返回结果
        return Result.success(hashMap.values().stream().toList());
    }

}
