package com.tiejun.demo.mapper;

import com.tiejun.demo.domain.TransactionRecord;

public interface TransactionRecordsMapper {
    int deleteByPrimaryKey(Long transactionId);

    int insert(TransactionRecord record);

    int insertSelective(TransactionRecord record);

    TransactionRecord selectByPrimaryKey(Long transactionId);

    int updateByPrimaryKeySelective(TransactionRecord record);

    int updateByPrimaryKey(TransactionRecord record);
}