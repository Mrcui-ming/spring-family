package com.atguigu.eduservice.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import feign.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = "com.atguigu.eduservice.mapper")
public class EduTeacherConfig {

  /**
   * 逻辑删除插件
   */
  @Bean
  public ISqlInjector sqlInjector() {
    return new LogicSqlInjector();
  }

  /**
   * 分页插件
   * */
  @Bean
  public PaginationInterceptor paginationInterceptor() {
    return new PaginationInterceptor();
  }

  /**
   * 乐观锁插件
   * */
  @Bean
  public OptimisticLockerInterceptor optimisticLockerInterceptor() {
    return new OptimisticLockerInterceptor();
  }

  /**
   * openFeign的注解日志输出
   * */
  @Bean
  Logger.Level feignLoggerLevel() {
    return  Logger.Level.FULL;
  }
}
