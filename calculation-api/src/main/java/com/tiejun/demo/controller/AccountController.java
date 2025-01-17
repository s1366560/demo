package com.tiejun.demo.controller;

import com.tiejun.demo.domain.Account;
import com.tiejun.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(
                account.getAccountNumber(),
                account.getBalance()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdAccount);
    }
}
