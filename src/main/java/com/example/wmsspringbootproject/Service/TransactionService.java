package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.model.form.TransactionsForm;
import com.example.wmsspringbootproject.model.vo.Result;

public interface TransactionService {
    Result saveTransaction(TransactionsForm form);
}
