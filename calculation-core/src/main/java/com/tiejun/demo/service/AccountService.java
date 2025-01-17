package com.tiejun.demo.service;

import com.tiejun.demo.domain.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account findByAccount(String accountNumber);

    Account createAccount(String accountNumber, BigDecimal balance);

    Account updateAccountBalance(String accountNumber, BigDecimal newSourceBalance);

    Account findById(Long id);
}
