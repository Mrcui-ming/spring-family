package com.atguigu.eduorder.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = "com.atguigu.eduorder.mapper")
public class OrderConfig {
}
