package com.tiejun.demo.service;

import com.tiejun.demo.config.IdGeneratorConfig;
import com.tiejun.demo.service.impl.SnowflakeTransactionIdGenerationServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionIdGenerationServiceTest {

    @Test
    void generateTransactionId() {
        IdGeneratorConfig idGeneratorConfig = new IdGeneratorConfig();
        idGeneratorConfig.setDataCenterId(1L);
        idGeneratorConfig.setWorkerId(1L);
        // 准备测试数据
        TransactionIdGenerationService transactionIdGenerationService = new SnowflakeTransactionIdGenerationServiceImpl(idGeneratorConfig);

        // 执行测试
        Long transactionId = transactionIdGenerationService.generateTransactionId();

        // 验证结果
        assertNotNull(transactionId);

        // 验证生成的ID是否唯一
        Long anotherId = transactionIdGenerationService.generateTransactionId();
        assertNotEquals(transactionId, anotherId);
    }
}