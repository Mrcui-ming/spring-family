package com.atguigu.aclservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@MapperScan("com.atguigu.aclservice.mapper")
public class AclConfig{

  private CorsConfiguration buildConfig() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    // 允许任何域名使用
    corsConfiguration.addAllowedOrigin("*");
    // 允许任何头
    corsConfiguration.addAllowedHeader("*");
    // 允许任何方法（post、get等）
    corsConfiguration.addAllowedMethod("*");
    //  携带cookie corsConfiguration.setAllowCredentials(true); 当前项目使用token不需要cookie

    return corsConfiguration;
  }


  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    // 对接口配置跨域设置
    source.registerCorsConfiguration("/**", buildConfig());
    return new CorsFilter(source);
  }

}
