package com.example.wmsspringbootproject.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.model.form.TransactionsForm;
import com.example.wmsspringbootproject.model.query.TransactionsQuery;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import com.example.wmsspringbootproject.model.vo.TransactionVO;
import com.example.wmsspringbootproject.common.result.Result;

public interface TransactionService {
    Result<IPage<TransactionVO>> transactionList(TransactionsQuery query);
    Result<Boolean> saveTransaction(TransactionsForm form);
    Result<Boolean> updateTransaction(TransactionsForm form);

    Result<Boolean> removeTransaction(String ids);

    Result<TransactionVO> getTransactionDetails(Long id);
}
