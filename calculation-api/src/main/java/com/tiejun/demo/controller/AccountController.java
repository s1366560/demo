package com.tiejun.demo.controller;

import com.tiejun.demo.common.Result;
import com.tiejun.demo.common.ResultCode;
import com.tiejun.demo.domain.Account;
import com.tiejun.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{account}")
    public Result<Account> getAccount(@PathVariable String account) {
        Account data = accountService.findByAccount(account);
        return data == null ? Result.error(ResultCode.NOT_FOUND) : Result.success(data);
    }

    @PostMapping
    public Result<Account> createAccount(@RequestBody Account account) {
        return Result.success(accountService.createAccount(account.getAccountNumber(), account.getBalance()));
    }

}
