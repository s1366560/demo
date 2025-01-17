package com.tiejun.demo.service;

import com.tiejun.demo.domain.Account;
import com.tiejun.demo.exception.BizException;
import com.tiejun.demo.mapper.AccountMapper;
import com.tiejun.demo.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountService accountService;
    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountMapper = mock(AccountMapper.class);
        accountService = new AccountServiceImpl(accountMapper);
    }

    @Test
    void findByAccount_shouldReturnAccount_whenAccountExists() {
        // given
        String accountNumber = "123456";
        Account expectedAccount = new Account(accountNumber, new BigDecimal("1000.00"));
        when(accountMapper.selectByAccount(accountNumber)).thenReturn(expectedAccount);

        // when
        Account actualAccount = accountService.findByAccount(accountNumber);

        // then
        assertThat(actualAccount).isEqualTo(expectedAccount);
        verify(accountMapper).selectByAccount(accountNumber);
    }

    @Test
    void findByAccount_shouldThrowException_whenAccountNotFound() {
        // given
        String accountId = "123456";
        when(accountMapper.selectByAccount(accountId)).thenReturn(null);

        // when & then
        assertThrows(BizException.class, () -> accountService.findByAccount(accountId));
        verify(accountMapper).selectByAccount(accountId);
    }

    @Test
    void createAccount_shouldSaveAccountSuccessfully() {
        // given
        String accountNumber = "123456";
        BigDecimal initialBalance = new BigDecimal("1000.00");
        Account newAccount = new Account(accountNumber, initialBalance);
        when(accountMapper.insert(accountNumber, initialBalance)).thenReturn(1);
        when(accountMapper.selectByAccount(accountNumber)).thenReturn(newAccount);

        // when
        Account createdAccount = accountService.createAccount(accountNumber, initialBalance);

        // then
        assertThat(createdAccount)
                .isEqualTo(newAccount);
    }

    @Test
    void updateAccountBalance_shouldUpdateBalance_whenAccountExists() {
        // given
        String accountNumber = "123456";
        Account existingAccount = new Account(accountNumber, new BigDecimal("1000.00"));
        BigDecimal newBalance = new BigDecimal("2000.00");
        when(accountMapper.selectByAccount(accountNumber)).thenReturn(existingAccount);
        when(accountMapper.updateByPrimaryKey(any(Account.class))).thenReturn(1);

        // when
        Account updatedAccount = accountService.updateAccountBalance(accountNumber, newBalance);

        // then
        assertThat(updatedAccount.getBalance()).isEqualTo(newBalance);
        verify(accountMapper).selectByAccount(accountNumber);
        verify(accountMapper).updateByPrimaryKey(any(Account.class));
    }

    @Test
    void updateAccountBalance_shouldThrowException_whenAccountNotFound() {
        // given
        String accountNumber = "123456";
        BigDecimal newBalance = new BigDecimal("2000.00");
        when(accountMapper.selectByAccount(accountNumber)).thenReturn(null);

        // when & then
        assertThrows(BizException.class,
                () -> accountService.updateAccountBalance(accountNumber, newBalance));
        verify(accountMapper).selectByAccount(accountNumber);
        verify(accountMapper, never()).updateByPrimaryKey(any(Account.class));
    }
}