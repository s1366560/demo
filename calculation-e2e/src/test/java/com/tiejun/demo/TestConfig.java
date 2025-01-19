package com.tiejun.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.tiejun.demo.controller",
        "com.tiejun.demo.service",
        "com.tiejun.demo.mapper",
        "com.tiejun.demo.config"
})
@MapperScan("com.tiejun.demo.mapper")
@EnableCaching
public class TestConfig {

} 