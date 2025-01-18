package com.tiejun.demo.mapper;

import com.tiejun.demo.domain.TransactionRecord;

public interface TransactionRecordsMapper {

    int insert(TransactionRecord record);

    TransactionRecord selectByPrimaryKey(Long transactionId);

    int updateByPrimaryKey(TransactionRecord record);
}