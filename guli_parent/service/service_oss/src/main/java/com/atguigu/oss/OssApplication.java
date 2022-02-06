package com.atguigu.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 排除数据源的扫描
 * 加入oss依赖之后 springboot默认会去扫描数据源的配置 而我们使用oss只做上传 不需要操作数据库
 *
 * @Component为了使用swagger2进行接口测试
 * */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.atguigu"})
@EnableDiscoveryClient
public class OssApplication {

  public static void main(String[] args) {
    SpringApplication.run(OssApplication.class, args);
  }
}

