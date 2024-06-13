package com.example.wmsspringbootproject.converter;

import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import com.example.wmsspringbootproject.model.entity.Transactions;
import com.example.wmsspringbootproject.model.form.TransactionsForm;
import com.example.wmsspringbootproject.model.vo.TransactionProductVO;
import com.example.wmsspringbootproject.model.vo.TransactionVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionProductConverter {
    TransactionProductVO entity2Vo(TransactionProduct entity);
}
