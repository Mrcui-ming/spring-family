package com.atguigu.educenter.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = "com.atguigu.educenter.mapper")
public class UcenterConfig {

  /**
   * 逻辑删除插件
   */
  @Bean
  public ISqlInjector sqlInjector() {
    return new LogicSqlInjector();
  }

}
