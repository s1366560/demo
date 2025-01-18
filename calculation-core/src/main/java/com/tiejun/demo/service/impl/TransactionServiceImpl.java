package com.tiejun.demo.service.impl;

import com.tiejun.demo.contsants.ErrorCode;
import com.tiejun.demo.domain.Account;
import com.tiejun.demo.domain.TransactionRecord;
import com.tiejun.demo.domain.TransactionStatus;
import com.tiejun.demo.exception.BizException;
import com.tiejun.demo.mapper.TransactionRecordsMapper;
import com.tiejun.demo.service.AccountService;
import com.tiejun.demo.service.TransactionService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRecordsMapper transactionRecordsMapper;
    private final AccountService accountService;

    public TransactionServiceImpl(TransactionRecordsMapper transactionRecordsMapper, AccountService accountService) {
        this.transactionRecordsMapper = transactionRecordsMapper;
        this.accountService = accountService;
    }

    @Transactional(rollbackFor = BizException.class)
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Override
    public TransactionRecord process(Long transactionId) {

        if (transactionId == null) {
            throw new IllegalArgumentException("transactionId is null");
        }

        TransactionRecord transactionRecord = transactionRecordsMapper.selectByPrimaryKey(transactionId);

        if (transactionRecord == null) {
            throw new BizException(ErrorCode.TRANSACTION_NOT_EXIST);
        }

        Account sourceAccount = accountService.findById(transactionRecord.getSourceAccountId());
        Account targetAccount = accountService.findById(transactionRecord.getTargetAccountId());
        BigDecimal amount = transactionRecord.getAmount();

        if (sourceAccount == null || targetAccount == null) {
            throw new BizException(ErrorCode.ACCOUNT_NOT_EXIST);
        }

        BigDecimal sourceBalance = sourceAccount.getBalance();
        if (sourceBalance.compareTo(amount) >= 0) {
            BigDecimal newSourceBalance = sourceBalance.subtract(amount);
            BigDecimal newTargetBalance = targetAccount.getBalance().add(amount);

            accountService.updateAccountBalance(sourceAccount.getAccountNumber(), newSourceBalance);
            accountService.updateAccountBalance(targetAccount.getAccountNumber(), newTargetBalance);

            transactionRecord.setStatus(TransactionStatus.SUCCESS);
            transactionRecord.setAmount(amount);
            transactionRecord.setTransactionTime(LocalDateTime.now());
            int result = transactionRecordsMapper.updateByPrimaryKey(transactionRecord);

            if (result == 1) {
                return transactionRecord;
            }

        } else {
            throw new IllegalArgumentException("amount not enough");
        }
        return null;
    }

    @Override
    public TransactionRecord create(TransactionRecord transactionRecord) {
        int result = transactionRecordsMapper.insert(transactionRecord);
        if (result != 1) {
            throw new BizException(ErrorCode.TRANSACTION_CREATE_ERROR);
        }
        return transactionRecord;
    }
}
