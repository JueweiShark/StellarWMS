package com.example.wmsspringbootproject.converter;

import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.entity.Products;
import com.example.wmsspringbootproject.model.form.ProductForm;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.vo.ProductTypeVO;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductTypeConverter {
   ProductTypeVO entity2Vo(ProductTypes entity);

}
