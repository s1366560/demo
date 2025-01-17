package com.tiejun.demo.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.tiejun.demo.config.IdGeneratorConfig;
import com.tiejun.demo.service.TransactionIdGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnowflakeTransactionIdGenerationServiceImpl implements TransactionIdGenerationService {

    private final Snowflake idUtil;


    @Autowired
    public SnowflakeTransactionIdGenerationServiceImpl(IdGeneratorConfig config) {
        this.idUtil = IdUtil.getSnowflake(config.getDataCenterId(),config.getWorkerId());
    }

    @Override
    public Long generateTransactionId() {
        return idUtil.nextId();
    }
}
