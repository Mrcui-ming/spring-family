package com.atguigu.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 这个配置类方便于我们直接通过 类名.属性 拿到配置文件中的配置信息
 * spring加载之后执行InitializingBean接口中的方法afterPropertiesSet
 * 相当于我们可以快捷的访问私有变量了
 * */
@Component
public class ConstantPropertiesUtils implements InitializingBean{

  //读取配置文件内容
  @Value("${aliyun.oss.file.endpoint}")
  private String endpoint;

  @Value("${aliyun.oss.file.keyid}")
  private String keyId;

  @Value("${aliyun.oss.file.keysecret}")
  private String keySecret;

  @Value("${aliyun.oss.file.bucketname}")
  private String bucketName;

  //定义公开静态常量
  public static String END_POIND;
  public static String ACCESS_KEY_ID;
  public static String ACCESS_KEY_SECRET;
  public static String BUCKET_NAME;

  @Override
  public void afterPropertiesSet() throws Exception {
    END_POIND = endpoint;
    ACCESS_KEY_ID = keyId;
    ACCESS_KEY_SECRET = keySecret;
    BUCKET_NAME = bucketName;
  }
}
