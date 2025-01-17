package com.tiejun.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.tiejun.demo.mapper")
public class MyBatisConfig {
}
