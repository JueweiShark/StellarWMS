package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.ProductTypeService;
import com.example.wmsspringbootproject.Service.TransactionProductService;
import com.example.wmsspringbootproject.Service.TransactionService;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.converter.TransactionConverter;
import com.example.wmsspringbootproject.mapper.ProductTypeMapper;
import com.example.wmsspringbootproject.mapper.TransactionMapper;
import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.entity.Products;
import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import com.example.wmsspringbootproject.model.entity.Transactions;
import com.example.wmsspringbootproject.model.form.TransactionsForm;
import com.example.wmsspringbootproject.model.query.TransactionsQuery;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.vo.TransactionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.util.TxUtils;
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
    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Override
    public Result<IPage<TransactionVO>> transactionList(TransactionsQuery query) {
        System.out.println(query);
        LambdaQueryWrapper<Transactions> queryWrapper=new LambdaQueryWrapper<>();
        if (query.getCreatorId()>0) {
            queryWrapper.eq(Transactions::getCreatorId, query.getCreatorId());
        }
        if (query.getConfirmatorId()>0) {
            queryWrapper.eq(Transactions::getConfirmatorId, query.getConfirmatorId());
        }
        if (query.getAuditorId()>0){
            queryWrapper.eq(Transactions::getAuditorId, query.getAuditorId());
        }
        if(query.getWarehouseId()>0){
            queryWrapper.eq(Transactions::getWarehouseId, query.getWarehouseId());
        }
        if (TextUtil.isNotEmpty(query.getDeleted())){
            queryWrapper.eq(Transactions::getDeleted,query.getDeleted());
        }
        Page<Transactions> transactionPage=new Page<>(query.getPageNum(),query.getPageSize());
        IPage<Transactions> transactionList =this.page(transactionPage,queryWrapper);
        IPage<TransactionVO> transactionVOIPage = new Page<>();
        transactionVOIPage.setRecords(transactionList.getRecords().stream().map(transaction -> {
            TransactionVO transactionVO = transactionConverter.entity2Vo(transaction);
            return transactionVO;
        }).toList());
        transactionVOIPage.setPages(transactionList.getPages());
        transactionVOIPage.setCurrent(transactionList.getCurrent());
        transactionVOIPage.setSize(transactionList.getSize());
        transactionVOIPage.setTotal(transactionList.getTotal());
        return Result.success(transactionVOIPage);
    }

    @Override
    public Result<Boolean> saveTransaction(TransactionsForm form) {
        System.out.println(form);
        Transactions transaction = transactionConverter.form2entity(form);
        System.out.println(transaction);
        String createTime = TextUtil.formatDate(new Date());
        transaction.setCreateTime(createTime);
        transaction.setUpdateTime(createTime);
        transaction.setStatus(transaction.getStatus()==0?1:transaction.getStatus()==1?2:3);
        transaction.setDeleted(1);
        if (TextUtil.isNotEmpty((int) form.getCreatorId())){
            transaction.setCreatorId(form.getCreatorId());
        }

        if (!this.save(transaction)){
            return Result.failed("保存失败");
        }

        StringBuilder ids = new StringBuilder();
        for(TransactionProduct product : form.getProductList()){
            System.out.println(product);
            product.setTransactionId(transaction.getId());
            System.out.println("*****************************"+product.getTypeId());
            if (!TextUtil.isNumeric(product.getTypeId())){
                ProductTypes productTypes = new ProductTypes();
                productTypes.setName(product.getTypeId());
                productTypeMapper.insert(productTypes);
                product.setTypeId(String.valueOf(productTypes.getId()));
            }
            Long productId = productService.saveTransactionProduct(product);
            if(productId==null){
                return Result.failed("保存失败");
            }
            ids.append(productId).append(',');
        }
        transaction.setProductIds(ids.toString());
        if (!this.updateById(transaction)){
            return Result.failed("保存失败");
        }
        return Result.success(true);
    }

    @Override
    public Result<Boolean> updateTransaction(TransactionsForm form) {
        Transactions transaction = this.getById(form.getId());
        if (transaction != null) {
            Transactions target = transactionConverter.form2entity(form);
            target.setUpdateTime(TextUtil.formatDate(new Date()));
            target.setStatus(transaction.getStatus() == 0 ? 1 : transaction.getStatus() == 1 ? 2 : 3);
//            target.setStatus(Integer.parseInt(form.getStatus()));
            if (!this.updateById(target)) {
                return Result.failed("保存失败");
            }

            Boolean result = this.productService.removeTransactionProduct(form.getId());

            if (result) {
                StringBuilder ids = new StringBuilder();
                for (TransactionProduct product : form.getProductList()) {
                    System.out.println(product);
                    product.setTransactionId(target.getId());
                    Long productId = productService.saveTransactionProduct(product);

                    if (productId == null) {
                        return Result.failed("保存失败");
                    }

                    ids.append(productId);
                    if (form.getProductList().indexOf(product) < form.getProductList().size() - 1) {
                        ids.append(',');
                    }
                }

                target.setProductIds(ids.toString());

                if (!this.updateById(target)) {
                    return Result.failed("保存失败");
                }
            }
//            if (form.getAuditorId() >0) {
////                遍历form中的productList
//                for (TransactionProduct product : form.getProductList()) {
//                    ProductTypes productTypes = productTypeMapper.selectById(product.getTypeId());
//                    if (productTypes==null) {
//                        pro
//                    }
//                }
//            }
            return Result.success(true);
        } else {
            return Result.failed("保存失败");
        }
    }

    @Override
    public Result<Boolean> removeTransaction(String ids) {
        String[] idArray=ids.split(",");
        if(idArray.length>1){
            for (String id : idArray) {
                Transactions transactions =this.getById(id);
                transactions.setDeleted(1);
                this.baseMapper.updateById(transactions);
            }
            return Result.success();
        }else{
            Transactions transactions =this.getById(ids);
            transactions.setDeleted(1);
            Boolean result=this.baseMapper.updateById(transactions)>0;
            return result ? Result.success(result) : Result.failed("删除失败");
        }
    }

    @Override
    public Result<TransactionVO> getTransactionDetails(String id) {
        Transactions transactions =this.getById(id);
        System.out.println(transactions);
        if (transactions == null) {
            return Result.failed("未查询到该产品");
        }
        return Result.success(transactionConverter.entity2Vo(transactions));
    }


}
