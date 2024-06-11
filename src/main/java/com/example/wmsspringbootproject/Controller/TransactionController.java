package com.example.wmsspringbootproject.Controller;

import com.example.wmsspringbootproject.Service.TransactionService;
import com.example.wmsspringbootproject.model.form.TransactionsForm;
import com.example.wmsspringbootproject.model.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "04.事务管理")
@CrossOrigin

@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @PostMapping("/add")
    @Operation(summary = "添加事务")
    public Result addTransaction(
            @RequestBody TransactionsForm form
            ) {
        System.out.println(form);
        return transactionService.saveTransaction(form);
    }
}
