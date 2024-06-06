package com.example.wmsspringbootproject.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.model.form.WareHouseForm;
import com.example.wmsspringbootproject.model.query.WarehouseQuery;
import com.example.wmsspringbootproject.model.vo.WareHouseVO;

import java.util.List;
import java.util.Set;

public interface WareHouseService {
    Result<IPage<WareHouseVO>> warehouseList(WarehouseQuery query);

    Result<Boolean> saveWareHouseInfo(WareHouseForm form);

    Result<Boolean> updateWareHouseInfo(WareHouseForm form);

    Result<Boolean> removeWareHouseInfo(String ids);

    Result<WareHouseVO> getWareHouseDetails(Integer id);
}
