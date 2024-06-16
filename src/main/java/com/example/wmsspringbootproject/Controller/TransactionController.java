package com.example.wmsspringbootproject.Controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.Service.TransactionProductService;
import com.example.wmsspringbootproject.Service.TransactionService;
import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import com.example.wmsspringbootproject.model.form.ProductForm;
import com.example.wmsspringbootproject.model.form.TransactionsForm;
import com.example.wmsspringbootproject.model.query.ProductQuery;
import com.example.wmsspringbootproject.model.query.TransactionsQuery;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.vo.TransactionProductVO;
import com.example.wmsspringbootproject.model.vo.TransactionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "04.事务管理")
@CrossOrigin

@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionProductService transactionProductService;
    @GetMapping("/list")
    @Operation(summary = "获取事务列表")
    public Result<IPage<TransactionVO>> listTransaction(
            @ParameterObject TransactionsQuery query
    ){
        return transactionService.transactionList(query);
    }
    @PostMapping("/add")
    @Operation(summary = "添加事务")
    public Result<Boolean> addTransaction(
            @RequestBody TransactionsForm form
            ) {
        System.out.println(form);
        return transactionService.saveTransaction(form);
    }
    @PutMapping("/update")
    @Operation(summary = "修改事务信息")
    public Result<Boolean> updateTransaction(
            @RequestBody TransactionsForm transactionsForm
    ){
        return transactionService.updateTransaction(transactionsForm);
    }
    @DeleteMapping("/delete/{ids}")
    @Operation(summary = "删除事务信息")
    public Result<Boolean> deleteTransactions(
            @Parameter(description="需要删除的ids多个id用','隔开") @PathVariable("ids") String ids
    ){
        return transactionService.removeTransaction(ids);
    }
    @GetMapping("/Details/{id}")
    @Operation(summary = "获取事务详情")
    public Result<TransactionVO> getTransactions(
            @PathVariable("id") Long id
    ){
        return transactionService.getTransactionDetails(id);
    }
    @GetMapping("/productList/{id}")
    @Operation(summary = "获取事务的产品列表")
    public Result<List<TransactionProductVO>> listTransactionProduct(
            @PathVariable("id") Long id
    ){
        return transactionProductService.getTransactionProductByTid(id);
    }
}
