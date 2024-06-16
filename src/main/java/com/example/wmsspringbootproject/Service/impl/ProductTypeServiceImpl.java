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
import com.example.wmsspringbootproject.mapper.ProductTypeMapper;
import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.entity.Products;
import com.example.wmsspringbootproject.model.vo.ProductTypeVO;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductTypes> implements ProductTypeService {
    private final ProductTypeConverter converter;

    @Override
    public Result<List<ProductTypeVO>> productTypeList(){
        LambdaQueryWrapper<ProductTypes> queryWrapper=new LambdaQueryWrapper<>();
        List<ProductTypes> productList =this.baseMapper.selectList(queryWrapper);
        List<ProductTypeVO> productTypeVO=productList.stream().map(converter::entity2Vo).toList();
        return Result.success(productTypeVO);
    };


}
