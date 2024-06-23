package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.form.ProductBreakageForm;
import com.example.wmsspringbootproject.model.query.ProductBreakageQuery;
import com.example.wmsspringbootproject.model.vo.ProductBreakageVO;

import java.util.List;

public interface ProductBreakageService {
    Result<Boolean> addProductBreakage(ProductBreakageQuery query);

    Result<List<ProductBreakageVO>> productBreakageList(ProductBreakageForm query);
}
