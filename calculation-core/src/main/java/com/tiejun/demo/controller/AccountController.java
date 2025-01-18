package com.tiejun.demo.controller;

import com.tiejun.demo.domain.Account;
import com.tiejun.demo.service.AccountService;
import com.tiejun.demo.vo.AccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account data = accountService.findByAccount(accountNumber);
        return data == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountVo account) {
        // 验证账户号不能为空
        if (account.getAccountNumber() == null || account.getAccountNumber().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // 验证余额不能为负数
        if (account.getBalance() == null || account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().build();
        }

        Account createdAccount = accountService.createAccount(
                account.getAccountNumber(),
                account.getBalance()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdAccount);
    }
}
