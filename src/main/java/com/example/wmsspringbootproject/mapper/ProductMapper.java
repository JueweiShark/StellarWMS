package com.example.wmsspringbootproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wmsspringbootproject.model.entity.Products;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ProductMapper extends BaseMapper<Products> {
}
