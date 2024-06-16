package com.example.wmsspringbootproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.vo.ProductTypeVO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ProductTypeMapper extends BaseMapper<ProductTypes> {
}
