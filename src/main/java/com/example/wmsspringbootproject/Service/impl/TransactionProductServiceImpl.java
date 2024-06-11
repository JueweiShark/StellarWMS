package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.TransactionProductService;
import com.example.wmsspringbootproject.mapper.TransactionProductMapper;
import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionProductServiceImpl extends ServiceImpl<TransactionProductMapper, TransactionProduct> implements TransactionProductService {

    @Override
    public Long saveTransactionProduct(TransactionProduct transactionProduct) {
        System.out.println(transactionProduct);
        this.save(transactionProduct);
//        获取刚新增的id
        return transactionProduct.getId();
    }
}
