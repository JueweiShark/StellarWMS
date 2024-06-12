package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.TransactionProductService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.converter.TransactionProductConverter;
import com.example.wmsspringbootproject.mapper.TransactionProductMapper;
import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import com.example.wmsspringbootproject.model.vo.TransactionProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionProductServiceImpl extends ServiceImpl<TransactionProductMapper, TransactionProduct> implements TransactionProductService {
    TransactionProductMapper mapper;
    private final TransactionProductConverter converter;

    @Override
    public Long saveTransactionProduct(TransactionProduct transactionProduct) {
        this.save(transactionProduct);
        return transactionProduct.getId();
    }
    @Override
    public Boolean removeTransactionProduct(Long id) {
        return this.lambdaUpdate().eq(TransactionProduct::getTransactionId,id).remove();
    }

    @Override
    public Result<List<TransactionProductVO>> getTransactionProductByTid(Long id) {
        List<TransactionProduct> transactionList = this.lambdaQuery().eq(TransactionProduct::getTransactionId, id).list();
        return Result.success(transactionList.stream()
                .map(transactionProduct -> {
                    TransactionProductVO vo = new TransactionProductVO();
                    vo.setId(String.valueOf(transactionProduct.getId()));
                    vo.setName(transactionProduct.getName());
                    vo.setNumber(transactionProduct.getNumber());
                    vo.setUtil(transactionProduct.getUtil());
                    vo.setTransactionId(String.valueOf(transactionProduct.getTransactionId()));
                    return vo;
                }).collect(Collectors.toList()));
    }
}
