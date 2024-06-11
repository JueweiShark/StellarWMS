package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.TransactionProductService;
import com.example.wmsspringbootproject.Service.TransactionService;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.converter.TransactionConverter;
import com.example.wmsspringbootproject.mapper.TransactionMapper;
import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import com.example.wmsspringbootproject.model.entity.Transactions;
import com.example.wmsspringbootproject.model.form.TransactionsForm;
import com.example.wmsspringbootproject.model.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transactions> implements TransactionService {
    @Autowired
    private TransactionProductService productService;
    @Autowired
    private TransactionConverter transactionConverter;
    @Override
    public Result saveTransaction(TransactionsForm form) {
        System.out.println(form.getProductList().toArray().length);

        Transactions transaction = transactionConverter.form2entity(form);
        System.out.println(transaction);
        String createTime = TextUtil.formatDate(new Date());
        transaction.setCreateTime(createTime);
        transaction.setUpdateTime(createTime);
        transaction.setStatus(transaction.getStatus()==0?1:transaction.getStatus()==1?2:3);

        if (!this.save(transaction)){
            return Result.fail("500","保存失败");
        }

        StringBuilder ids = new StringBuilder();
        for(TransactionProduct product : form.getProductList()){
            System.out.println(product);
            product.setTransactionId(transaction.getId());
            Long productId = productService.saveTransactionProduct(product);
            if(productId==null){
                return Result.fail("500", "保存失败");
            }
            ids.append(productId).append(',');
        }

        System.out.println(transaction);
        transaction.setProductIds(ids.toString());
        if (!this.updateById(transaction)){
            return Result.fail("500","保存失败");
        }
        return Result.success(true);
    }
}
