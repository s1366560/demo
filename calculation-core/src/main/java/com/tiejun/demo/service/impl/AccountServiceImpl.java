package com.tiejun.demo.service.impl;

import com.tiejun.demo.domain.Account;
import com.tiejun.demo.mapper.AccountMapper;
import com.tiejun.demo.service.AccountService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional
    @Cacheable(key = "#account", value = "accounts")
    public Account findByAccount(String account) {
        return accountMapper.selectByAccount(account);
    }

    @Override
    @Transactional
    public Account createAccount(String accountNumber, BigDecimal balance) {
        return accountMapper.insert(accountNumber, balance);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#account.accountNumber", value = "accounts")
    public Account updateAccountBalance(Account account, BigDecimal newSourceBalance) {
        account.setBalance(newSourceBalance);
        accountMapper.updateByPrimaryKey(account);
        return account;
    }
}
