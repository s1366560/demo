package com.tiejun.demo.service.impl;

import com.tiejun.demo.contsants.ErrorCode;
import com.tiejun.demo.domain.Account;
import com.tiejun.demo.exception.BizException;
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
        Account result = accountMapper.selectByAccount(account);
        if (null == result) {
            throw new BizException(ErrorCode.ACCOUNT_NOT_EXIST);
        }
        return result;
    }

    @Override
    @Transactional
    public Account createAccount(String accountNumber, BigDecimal balance) {
        if (accountMapper.insert(accountNumber, balance) == 1) {
            return accountMapper.selectByAccount(accountNumber);
        }
        return null;
    }

    @Override
    @Transactional
    @CacheEvict(key = "#accountNumber", value = "accounts")
    public Account updateAccountBalance(String accountNumber, BigDecimal newSourceBalance) {
        Account account = accountMapper.selectByAccount(accountNumber);
        if (account == null) {
            throw new BizException(ErrorCode.ACCOUNT_NOT_EXIST);
        }
        account.setBalance(newSourceBalance);
        accountMapper.updateByPrimaryKey(account);
        return account;
    }

    @Override
    public Account findById(Long id) {
        return accountMapper.selectByPrimaryKey(id);
    }
}
