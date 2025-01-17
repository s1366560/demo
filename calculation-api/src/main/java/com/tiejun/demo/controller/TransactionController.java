package com.tiejun.demo.controller;

import com.tiejun.demo.domain.Account;
import com.tiejun.demo.domain.TransactionRecord;
import com.tiejun.demo.domain.TransactionStatus;
import com.tiejun.demo.dto.TransactionRequest;
import com.tiejun.demo.dto.TransactionResult;
import com.tiejun.demo.service.AccountService;
import com.tiejun.demo.service.TransactionIdGenerationService;
import com.tiejun.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
    public ResponseEntity<TransactionResult> transfer(@RequestBody TransactionRequest request) {
        // 验证请求参数
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResult("转账金额必须大于0"));
        }

        if (request.getSourceAccountNumber() == null || request.getTargetAccountNumber() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResult("账户号码不能为空"));
        }

        if (request.getSourceAccountNumber().equals(request.getTargetAccountNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResult("源账户和目标账户不能相同"));
        }

        try {
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
            TransactionRecord result = transactionService.process(transactionRecord.getTransactionId());

            TransactionResult data = new TransactionResult();
            data.setTransactionId(String.format("%s", result.getTransactionId()));
            data.setTransactionAmount(result.getAmount().toString());
            data.setTransactionStatus(result.getStatus());
            data.setTransactionDate(result.getCreateTime().toString());

            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResult(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResult("交易处理失败：" + e.getMessage()));
        }
    }

    private TransactionResult createErrorResult(String message) {
        TransactionResult result = new TransactionResult();
        result.setTransactionStatus(TransactionStatus.FAILED);
        result.setErrorMessage(message);
        return result;
    }
}
