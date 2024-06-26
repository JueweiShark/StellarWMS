package com.example.wmsspringbootproject.converter;


import com.example.wmsspringbootproject.model.entity.Transactions;
import com.example.wmsspringbootproject.model.form.TransactionsForm;
import com.example.wmsspringbootproject.model.vo.TransactionVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionConverter {
    TransactionsForm entity2Form(Transactions entity);
    TransactionVO entity2Vo(Transactions entity);

    Transactions Vo2entity(TransactionVO vo);

    Transactions form2entity(TransactionsForm form);
}
