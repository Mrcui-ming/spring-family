package com.atguigu.educms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient  //nacos注册
@ComponentScan(basePackages = {"com.atguigu"})
public class CrmBannerApplication {

  public static void main(String[] args) {
    SpringApplication.run(CrmBannerApplication.class);
  }

}
