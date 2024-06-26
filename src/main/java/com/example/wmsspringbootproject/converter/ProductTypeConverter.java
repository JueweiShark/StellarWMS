package com.example.wmsspringbootproject.converter;

import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.vo.ProductTypeVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductTypeConverter {
   ProductTypeVO entity2Vo(ProductTypes entity);

}
