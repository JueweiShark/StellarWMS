package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.TransactionProductService;
import com.example.wmsspringbootproject.Service.TransactionService;
import com.example.wmsspringbootproject.Service.TransactionTypesService;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.converter.TransactionConverter;
import com.example.wmsspringbootproject.mapper.ProductMapper;
import com.example.wmsspringbootproject.mapper.ProductTypeMapper;
import com.example.wmsspringbootproject.mapper.TransactionMapper;
import com.example.wmsspringbootproject.mapper.TransactionTypesMapper;
import com.example.wmsspringbootproject.model.entity.*;
import com.example.wmsspringbootproject.model.form.TransactionsForm;
import com.example.wmsspringbootproject.model.query.TransactionsQuery;
import com.example.wmsspringbootproject.model.vo.TransactionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionTypesServiceImpl extends ServiceImpl<TransactionTypesMapper, TransactionTypes> implements TransactionTypesService {
    @Override
    public Result<List<TransactionTypes>> getTypesList() {
        LambdaQueryWrapper<TransactionTypes> queryWrapper=new LambdaQueryWrapper<>();
        List<TransactionTypes> productList =this.baseMapper.selectList(queryWrapper);
        return Result.success(productList);
    }

    @Override
    public TransactionTypes SelectNameById(long id) {
        return this.lambdaQuery().eq(TransactionTypes::getId,id).one();
    }
}
