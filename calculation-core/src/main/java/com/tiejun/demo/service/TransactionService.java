package com.tiejun.demo.service;

import com.tiejun.demo.domain.TransactionRecord;
import com.tiejun.demo.dto.TransactionRequestDto;
import com.tiejun.demo.dto.TransactionResult;

public interface TransactionService {

    TransactionRecord process(Long transactionId);

    TransactionRecord create(TransactionRecord transactionRecord);
}
