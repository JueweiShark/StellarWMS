package com.example.wmsspringbootproject.converter;

import com.example.wmsspringbootproject.model.entity.Warehouses;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.form.WareHouseForm;
import com.example.wmsspringbootproject.model.vo.WareHouseVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WareHouseConverter {
    WareHouseForm entity2Form(Warehouses entity);
    WareHouseVO entity2Vo(Warehouses entity);

    Warehouses Vo2entity(WareHouseVO vo);

    Warehouses form2Entity(WareHouseForm form);
}
