package com.tiejun.demo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@EnableConfigurationProperties(IdGeneratorConfig.class)
@Configuration
public class AutoConfiguration {

}
