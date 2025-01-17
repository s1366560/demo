package com.tiejun.demo.controller;

import com.tiejun.demo.common.Result;
import com.tiejun.demo.dto.TransactionRequestDto;
import com.tiejun.demo.dto.TransactionResponseDto;
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

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Result<TransactionResponseDto> transfer(@RequestBody TransactionRequestDto request) {
        return Result.success(transactionService.process(request));
    }

}
