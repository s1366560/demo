package com.tiejun.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiejun.demo.domain.Account;
import com.tiejun.demo.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("123456");
        testAccount.setBalance(new BigDecimal("100.0"));
    }

    @Test
    void testGetAccount() throws Exception {
        // 准备测试数据
        when(accountService.findByAccount(anyString())).thenReturn(testAccount);

        // 执行测试
        mockMvc.perform(get("/api/accounts/{accountNumber}", testAccount.getAccountNumber()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testAccount.getId()))
                .andExpect(jsonPath("$.accountNumber").value(testAccount.getAccountNumber()))
                .andExpect(jsonPath("$.balance").value(testAccount.getBalance().toString()));
    }

    @Test
    void testGetAccountNotFound() throws Exception {
        // 准备测试数据
        when(accountService.findByAccount(anyString())).thenReturn(null);

        // 执行测试
        mockMvc.perform(get("/api/accounts/{accountNumber}", "nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAccount() throws Exception {
        // 准备测试数据
        Account newAccount = new Account();
        newAccount.setAccountNumber("654321");
        newAccount.setBalance(new BigDecimal("50.0"));

        when(accountService.createAccount(anyString(), any(BigDecimal.class)))
                .thenReturn(newAccount);

        // 执行测试
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value(newAccount.getAccountNumber()))
                .andExpect(jsonPath("$.balance").value(newAccount.getBalance().toString()));
    }

    @Test
    void testCreateAccountWithInvalidData() throws Exception {
        // 准备测试数据 - 账户号为空的情况
        Account invalidAccount = new Account();
        invalidAccount.setBalance(new BigDecimal("50.0"));

        // 执行测试
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAccount)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateAccountWithNegativeBalance() throws Exception {
        // 准备测试数据 - 负余额的情况
        Account invalidAccount = new Account();
        invalidAccount.setAccountNumber("654321");
        invalidAccount.setBalance(new BigDecimal("-50.0"));

        // 执行测试
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAccount)))
                .andExpect(status().isBadRequest());
    }

}