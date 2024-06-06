package com.example.wmsspringbootproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wmsspringbootproject.common.Annotation.DataPermission;
import com.example.wmsspringbootproject.model.entity.Warehouses;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DataPermission(warehouseIdColumnName="id")
public interface WareHouseMapper extends BaseMapper<Warehouses> {



}
