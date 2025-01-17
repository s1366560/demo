package com.tiejun.demo.service;

import com.tiejun.demo.dto.TransactionRequestDto;
import com.tiejun.demo.dto.TransactionResponseDto;

public interface TransactionService {

    TransactionResponseDto process(TransactionRequestDto request);
}
