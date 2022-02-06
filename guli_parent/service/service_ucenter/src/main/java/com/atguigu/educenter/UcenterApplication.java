package com.atguigu.educenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient  //nacos注册
@ComponentScan(basePackages = {"com.atguigu"})
public class UcenterApplication {

  public static void main(String[] args) {
    SpringApplication.run(UcenterApplication.class);
  }
}
