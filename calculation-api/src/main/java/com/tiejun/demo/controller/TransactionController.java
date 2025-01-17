package com.tiejun.demo.controller;

import com.tiejun.demo.common.Result;
import com.tiejun.demo.domain.Account;
import com.tiejun.demo.domain.TransactionRecord;
import com.tiejun.demo.domain.TransactionStatus;
import com.tiejun.demo.dto.TransactionRequestDto;
import com.tiejun.demo.service.AccountService;
import com.tiejun.demo.service.TransactionIdGenerationService;
import com.tiejun.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final TransactionIdGenerationService transactionIdGenerationService;

    @Autowired
    public TransactionController(TransactionService transactionService, AccountService accountService, TransactionIdGenerationService transactionIdGenerationService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.transactionIdGenerationService = transactionIdGenerationService;
    }

    @PostMapping
    public Result<TransactionRecord> transfer(@RequestBody TransactionRequestDto request) {

        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setAmount(request.getAmount());
        Account sourceAccount = accountService.findByAccount(request.getSourceAccountNumber());
        Account targetAccount = accountService.findByAccount(request.getTargetAccountNumber());
        transactionRecord.setSourceAccountId(sourceAccount.getId());
        transactionRecord.setTargetAccountId(targetAccount.getId());
        transactionRecord.setAmount(request.getAmount());
        transactionRecord.setStatus(TransactionStatus.PENDING);
        transactionRecord.setTransactionId(transactionIdGenerationService.generateTransactionId());

        transactionService.create(transactionRecord);

        return Result.success(transactionService.process(transactionRecord.getTransactionId()));
    }

}
