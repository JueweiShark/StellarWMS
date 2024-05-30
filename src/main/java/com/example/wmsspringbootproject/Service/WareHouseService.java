package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.form.WareHouseForm;
import com.example.wmsspringbootproject.model.query.WarehouseQuery;
import com.example.wmsspringbootproject.model.vo.WareHouseVO;

import java.util.List;

public interface WareHouseService {
    Result<List<WareHouseVO>> warehouseList(WarehouseQuery query);

    Result<Boolean> saveWareHouseInfo(WareHouseForm form);

    Result<Boolean> updateWareHouseInfo(WareHouseForm form);

    Result<Boolean> removeWareHouseInfo(String ids);

    Result<WareHouseVO> getWareHouseDetails(Integer id);

}
