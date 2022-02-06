package com.atguigu.servicebase;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  /**
   * Swagger插件 由于每个项目做完都要测试 所以把swagger工具配置搭配公共的地方 比较方便
   * */
  @Bean
  public Docket webApiConfig(){
    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("webApi") //组名
            .apiInfo(webApiInfo()) //引入一个方法这个方法定义了在线文档的一些信息
            .select()
            .paths(Predicates.not(PathSelectors.regex("/admin/.*"))) //如果是、admin/*或者error/*请求 那么就无不进行显示            .paths(Predicates.not(PathSelectors.regex("/error.*")))
            .build();//最后打包
  }

  private ApiInfo webApiInfo(){

    return new ApiInfoBuilder()
            .title("教育网站-API文档")//标题
            .description("本文档描述了课程中心微服务接口定义")//简介
            .version("1.0")//版本
            .contact(new Contact("java", "https://github.com/Mrcui-ming", "375743077@qq.com"))//远程地址
            .build();//打包
  }

}
