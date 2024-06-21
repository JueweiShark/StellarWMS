package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.ProductBreakageService;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.converter.ProductBreakageConverter;
import com.example.wmsspringbootproject.mapper.ProductBreakageMapper;
import com.example.wmsspringbootproject.model.entity.ProductBreakage;
import com.example.wmsspringbootproject.model.form.ProductBreakageForm;
import com.example.wmsspringbootproject.model.query.ProductBreakageQuery;
import com.example.wmsspringbootproject.model.vo.ProductBreakageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductBreakageServiceImpl extends ServiceImpl<ProductBreakageMapper, ProductBreakage> implements ProductBreakageService {
    @Autowired
    private ProductBreakageConverter productBreakageConverter;
    @Override
    public Result<Boolean> addProductBreakage(ProductBreakageQuery query) {
        for (ProductBreakageForm productBreakageForm: query.getProductBreakageForms()) {
            ProductBreakage productBreakage = productBreakageConverter.form2entity(productBreakageForm);
            System.out.println(productBreakage);
            if (productBreakage.getId()==0){
                productBreakage.setCreateTime(TextUtil.formatDate(new Date()));
                if(!this.save(productBreakage)){
                    return Result.failed("保存失败");
                }
            }
        }
        return Result.success();
    }

    @Override
    public Result<List<ProductBreakageVO>> productBreakageList(ProductBreakageForm form) {
        LambdaQueryWrapper<ProductBreakage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(ProductBreakage::getCreateTime,form.getStartTime());
        queryWrapper.le(ProductBreakage::getCreateTime,form.getEndTime());
        if (form.getWarehouseId()>0){
            queryWrapper.eq(ProductBreakage::getWarehouseId,form.getWarehouseId());
        }
        List<ProductBreakage> productBreakageList = this.list(queryWrapper);
        List<ProductBreakageVO> productBreakageVOList =new ArrayList<>();
        for (ProductBreakage productBreakage : productBreakageList) {
            productBreakageVOList.add(productBreakageConverter.entity2VO(productBreakage));
        }
        return Result.success(productBreakageVOList);
    }
}
