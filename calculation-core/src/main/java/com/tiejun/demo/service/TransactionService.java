package com.tiejun.demo.service;

import com.tiejun.demo.domain.TransactionRecord;

public interface TransactionService {

    TransactionRecord process(Long transactionId);

    TransactionRecord create(TransactionRecord transactionRecord);
}
