package com.example.wmsspringbootproject.converter;

import com.example.wmsspringbootproject.model.entity.Products;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.form.ProductForm;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductConverter {
    ProductForm entity2Form(Products entity);
    ProductVO entity2Vo(Products entity);

    Products Vo2entity(ProductVO vo);

    Products form2Entity(ProductForm form);
}
