package com.example.wmsspringbootproject.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.TransactionTypes;
import com.example.wmsspringbootproject.model.form.TransactionsForm;
import com.example.wmsspringbootproject.model.query.TransactionsQuery;
import com.example.wmsspringbootproject.model.vo.TransactionVO;

import java.util.List;

public interface TransactionTypesService {
    Result<List<TransactionTypes>> getTypesList();
    TransactionTypes SelectNameById(long id);
}
