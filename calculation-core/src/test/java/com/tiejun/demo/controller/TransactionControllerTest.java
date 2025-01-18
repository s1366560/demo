package com.tiejun.demo.controller;

import com.tiejun.demo.domain.Account;
import com.tiejun.demo.domain.TransactionRecord;
import com.tiejun.demo.domain.TransactionStatus;
import com.tiejun.demo.dto.TransactionRequest;
import com.tiejun.demo.dto.TransactionResult;
import com.tiejun.demo.service.AccountService;
import com.tiejun.demo.service.TransactionIdGenerationService;
import com.tiejun.demo.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionIdGenerationService transactionIdGenerationService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionRequest validRequest;
    private Account sourceAccount;
    private Account targetAccount;
    private TransactionRecord transactionRecord;

    @BeforeEach
    void setUp() {
        // 设置请求数据
        validRequest = new TransactionRequest();
        validRequest.setSourceAccountNumber("ACC001");
        validRequest.setTargetAccountNumber("ACC002");
        validRequest.setAmount(new BigDecimal("100.00"));

        // 设置账户数据
        sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setAccountNumber("ACC001");

        targetAccount = new Account();
        targetAccount.setId(2L);
        targetAccount.setAccountNumber("ACC002");

        // 设置交易记录
        transactionRecord = new TransactionRecord();
        transactionRecord.setTransactionId(1L);
        transactionRecord.setAmount(new BigDecimal("100.00"));
        transactionRecord.setStatus(TransactionStatus.SUCCESS);
        transactionRecord.setCreateTime(LocalDateTime.now());
    }

    @Test
    void transfer_Success() {
        // Given
        when(accountService.findByAccount("ACC001")).thenReturn(sourceAccount);
        when(accountService.findByAccount("ACC002")).thenReturn(targetAccount);
        when(transactionIdGenerationService.generateTransactionId()).thenReturn(1L);
        when(transactionService.create(any(TransactionRecord.class))).thenReturn(transactionRecord);
        when(transactionService.process(1L)).thenReturn(transactionRecord);

        // When
        ResponseEntity<TransactionResult> response = transactionController.transfer(validRequest);

        // Then
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getTransactionId());
        assertEquals("100.00", response.getBody().getTransactionAmount());
        assertEquals(TransactionStatus.SUCCESS, response.getBody().getTransactionStatus());
    }

    @Test
    void transfer_WithInvalidAmount_ShouldReturnBadRequest() {
        // Given
        validRequest.setAmount(new BigDecimal("-100.00"));
        // When
        ResponseEntity<TransactionResult> response = transactionController.transfer(validRequest);

        // Then
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void transfer_WithSameAccounts_ShouldReturnBadRequest() {
        // Given
        validRequest.setTargetAccountNumber(validRequest.getSourceAccountNumber());
        // When
        ResponseEntity<TransactionResult> response = transactionController.transfer(validRequest);
        // Then
        assertTrue(response.getStatusCode().is4xxClientError());
    }
}