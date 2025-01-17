package com.tiejun.demo.service;

import com.tiejun.demo.domain.Account;
import com.tiejun.demo.domain.TransactionRecord;
import com.tiejun.demo.domain.TransactionStatus;
import com.tiejun.demo.mapper.TransactionRecordsMapper;
import com.tiejun.demo.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    private TransactionService transactionService;

    private AccountService accountService;

    private TransactionRecordsMapper transactionRecordMapper;

    private TransactionIdGenerationService transactionIdGenerationService;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        transactionRecordMapper = mock(TransactionRecordsMapper.class);
        transactionIdGenerationService = mock(TransactionIdGenerationService.class);
        transactionService = new TransactionServiceImpl(transactionRecordMapper, accountService, transactionIdGenerationService);
    }

    @Test
    void trans_shouldSucceed() {
        // 准备测试数据
        TransactionRecord transaction = new TransactionRecord();
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setSourceAccountId(1L);
        transaction.setTargetAccountId(2L);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionId(transactionIdGenerationService.generateTransactionId());

        when(accountService.findById(1L))
                .thenReturn(new Account(1L, "ACC01", BigDecimal.valueOf(100)));
        when(accountService.findById(2L))
                .thenReturn(new Account(2L, "ACC02", BigDecimal.valueOf(0)));

        when(transactionIdGenerationService.generateTransactionId()).thenReturn(1L);
        when(transactionRecordMapper.selectByPrimaryKey(transaction.getTransactionId())).thenReturn(transaction);
        when(transactionRecordMapper.updateByPrimaryKey(transaction)).thenReturn(1);

        // 执行测试
        TransactionRecord result = transactionService.process(transaction.getTransactionId());

        // 验证结果
        assertNotNull(result);
        assertEquals(TransactionStatus.SUCCESS, result.getStatus());
        assertEquals(BigDecimal.valueOf(100.0), result.getAmount());
    }
}