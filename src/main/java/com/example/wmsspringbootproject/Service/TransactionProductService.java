package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import com.example.wmsspringbootproject.model.vo.TransactionProductVO;

import java.util.List;

public interface TransactionProductService {
    Long saveTransactionProduct(TransactionProduct transactionProduct);
    Boolean removeTransactionProduct(Long id);
    Result<List<TransactionProductVO>> getTransactionProductByTid(Long id);
}
