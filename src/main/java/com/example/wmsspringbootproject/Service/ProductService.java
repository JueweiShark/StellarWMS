package com.example.wmsspringbootproject.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.form.ProductForm;
import com.example.wmsspringbootproject.model.form.WareHouseForm;
import com.example.wmsspringbootproject.model.query.ProductQuery;
import com.example.wmsspringbootproject.model.query.WarehouseQuery;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import com.example.wmsspringbootproject.model.vo.WareHouseVO;

public interface ProductService {
    Result<IPage<ProductVO>> productList(ProductQuery query);

    Result<Boolean> saveProductInfo(ProductForm form);

    Result<Boolean> updateProductInfo(ProductForm form);

    Result<Boolean> removeProductInfo(String ids);

    Result<ProductVO> getProductDetails(Integer id);

}
