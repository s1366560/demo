package com.tiejun.demo.e2e;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.tiejun.demo")
@MapperScan("com.tiejun.demo.mapper")
public class TestConfig {

} 