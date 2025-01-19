package com.tiejun.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@EnableConfigurationProperties(IdGeneratorConfig.class)
@Configuration
public class AutoConfiguration {

    @Configuration
    @MapperScan("com.tiejun.demo.mapper")
    static class MyBatisConfig {
    }

    @Configuration
    @EnableCaching
    static class RedisConfig {
    }
}
