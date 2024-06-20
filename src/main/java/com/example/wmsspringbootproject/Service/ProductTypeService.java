package com.example.wmsspringbootproject.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.query.WarehouseQuery;
import com.example.wmsspringbootproject.model.vo.ProductTypeVO;
import com.example.wmsspringbootproject.model.vo.WareHouseVO;

import java.util.List;

public interface ProductTypeService {

   Result<List<ProductTypeVO>> productTypeList();
}
