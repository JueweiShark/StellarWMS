package com.example.wmsspringbootproject.converter;



import com.example.wmsspringbootproject.model.entity.ProductBreakage;
import com.example.wmsspringbootproject.model.form.ProductBreakageForm;
import com.example.wmsspringbootproject.model.vo.ProductBreakageVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductBreakageConverter {
    ProductBreakage form2entity(ProductBreakageForm productBreakageForm);
    ProductBreakageVO entity2VO(ProductBreakage entity);
}
