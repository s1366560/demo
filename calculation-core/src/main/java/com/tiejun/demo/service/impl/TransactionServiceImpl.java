package com.tiejun.demo.service.impl;

import com.tiejun.demo.contsants.ErrorCode;
import com.tiejun.demo.domain.Account;
import com.tiejun.demo.domain.TransactionRecord;
import com.tiejun.demo.dto.TransactionRequestDto;
import com.tiejun.demo.dto.TransactionResponseDto;
import com.tiejun.demo.exception.BizException;
import com.tiejun.demo.mapper.TransactionRecordsMapper;
import com.tiejun.demo.service.AccountService;
import com.tiejun.demo.service.TransactionIdGenerationService;
import com.tiejun.demo.service.TransactionService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRecordsMapper transactionRecordsMapper;
    private final AccountService accountService;
    private final TransactionIdGenerationService idGenerationService;

    public TransactionServiceImpl(TransactionRecordsMapper transactionRecordsMapper, AccountService accountService, TransactionIdGenerationService idGenerationService) {
        this.transactionRecordsMapper = transactionRecordsMapper;
        this.accountService = accountService;
        this.idGenerationService = idGenerationService;
    }

    @Transactional(rollbackFor = BizException.class)
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Override
    public TransactionResponseDto process(TransactionRequestDto request) {
        Account sourceAccount = accountService.findByAccount(request.getSourceAccountNumber());
        Account targetAccount = accountService.findByAccount(request.getTargetAccountNumber());
        BigDecimal amount = request.getAmount();
        if (sourceAccount == null || targetAccount == null) {
            throw new BizException(ErrorCode.ACCOUNT_NOT_EXIST);
        }

        BigDecimal sourceBalance = sourceAccount.getBalance();
        if (sourceBalance.compareTo(amount) >= 0) {
            BigDecimal newSourceBalance = sourceBalance.subtract(amount);
            BigDecimal newTargetBalance = targetAccount.getBalance().add(amount);

            accountService.updateAccountBalance(sourceAccount, newSourceBalance);
            accountService.updateAccountBalance(targetAccount, newTargetBalance);

            TransactionRecord transaction = new TransactionRecord();
            transaction.setTransactionId(transaction.getTransactionId());
            transaction.setSourceAccountId(sourceAccount.getId());
            transaction.setTargetAccountId(targetAccount.getId());
            transaction.setAmount(amount);

            int result = transactionRecordsMapper.insert(transaction);
        }
        return null;
    }
}
