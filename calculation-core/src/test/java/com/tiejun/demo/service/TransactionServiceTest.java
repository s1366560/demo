package com.tiejun.demo.service;

import com.tiejun.demo.contsants.ErrorCode;
import com.tiejun.demo.domain.Account;
import com.tiejun.demo.domain.TransactionRecord;
import com.tiejun.demo.domain.TransactionStatus;
import com.tiejun.demo.exception.BizException;
import com.tiejun.demo.mapper.TransactionRecordsMapper;
import com.tiejun.demo.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
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
        transactionService = new TransactionServiceImpl(transactionRecordMapper, accountService);
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

    @Test
    void trans_shouldFail_whenInsufficientBalance() {
        // 准备测试数据
        TransactionRecord transaction = new TransactionRecord();
        transaction.setAmount(BigDecimal.valueOf(200.0));
        transaction.setSourceAccountId(1L);
        transaction.setTargetAccountId(2L);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionId(1L);

        when(accountService.findById(1L))
                .thenReturn(new Account(1L, "ACC01", BigDecimal.valueOf(100)));
        when(accountService.findById(2L))
                .thenReturn(new Account(2L, "ACC02", BigDecimal.valueOf(0)));
        when(transactionRecordMapper.selectByPrimaryKey(transaction.getTransactionId())).thenReturn(transaction);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.process(transaction.getTransactionId()));
        assertEquals("amount not enough", exception.getMessage());
    }

    @Test
    void trans_shouldFail_whenAccountNotExist() {
        // 准备测试数据
        TransactionRecord transaction = new TransactionRecord();
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setSourceAccountId(1L);
        transaction.setTargetAccountId(2L);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionId(1L);

        when(transactionRecordMapper.selectByPrimaryKey(transaction.getTransactionId())).thenReturn(transaction);

        // 执行测试并验证异常
        when(accountService.findById(1L)).thenReturn(null);
        when(accountService.findById(2L)).thenReturn(new Account(2L, "ACC02", BigDecimal.valueOf(0)));

        BizException exception1 = assertThrows(BizException.class,
                () -> transactionService.process(transaction.getTransactionId()));
        assertEquals(ErrorCode.ACCOUNT_NOT_EXIST, exception1.getErrorCode());

        when(accountService.findById(2L)).thenReturn(null);
        when(accountService.findById(1L)).thenReturn(new Account(2L, "ACC02", BigDecimal.valueOf(0)));

        BizException exception2 = assertThrows(BizException.class,
                () -> transactionService.process(transaction.getTransactionId()));
        assertEquals(ErrorCode.ACCOUNT_NOT_EXIST, exception2.getErrorCode());

        when(accountService.findById(1L)).thenReturn(null);
        when(accountService.findById(2L)).thenReturn(null);

        BizException exception = assertThrows(BizException.class,
                () -> transactionService.process(transaction.getTransactionId()));
        assertEquals(ErrorCode.ACCOUNT_NOT_EXIST, exception.getErrorCode());
    }

    @Test
    void trans_shouldFail_whenTransactionNotExist() {
        when(transactionRecordMapper.selectByPrimaryKey(1L)).thenReturn(null);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class,
                () -> transactionService.process(1L));
        assertEquals(ErrorCode.TRANSACTION_NOT_EXIST, exception.getErrorCode());
    }

    @Test
    void trans_shouldFail_whenTransactionIsNull() {
        when(transactionRecordMapper.selectByPrimaryKey(1L)).thenReturn(null);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.process(null));
        assertEquals("transactionId is null", exception.getMessage());
    }

    @Test
    void create_shouldSucceed() {
        // 准备测试数据
        TransactionRecord transaction = new TransactionRecord();
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setSourceAccountId(1L);
        transaction.setTargetAccountId(2L);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionId(1L);

        when(transactionRecordMapper.insert(transaction)).thenReturn(1);

        // 执行测试
        TransactionRecord result = transactionService.create(transaction);

        // 验证结果
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100.0), result.getAmount());
        assertEquals(TransactionStatus.PENDING, result.getStatus());
    }

    @Test
    void create_shouldFail() {
        // 准备测试数据
        TransactionRecord transaction = new TransactionRecord();
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setSourceAccountId(1L);
        transaction.setTargetAccountId(2L);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionId(1L);

        when(transactionRecordMapper.insert(transaction)).thenReturn(0);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class,
                () -> transactionService.create(transaction));
        assertEquals(ErrorCode.TRANSACTION_CREATE_ERROR, exception.getErrorCode());
    }


}