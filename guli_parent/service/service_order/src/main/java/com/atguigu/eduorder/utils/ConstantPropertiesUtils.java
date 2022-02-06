package com.atguigu.eduorder.utils;

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
  @Value("${weixin.pay.appid}")
  private String appid;

  @Value("${weixin.pay.partner}")
  private String partner;

  @Value("${weixin.pay.partnerkey}")
  private String partnerkey;

  @Value("${weixin.pay.notifyurl}")
  private String notifyurl;

  //定义公开静态常量
  public static String APP_ID;
  public static String PARTNER;
  public static String PARTNER_KEY;
  public static String NOTIFY_URL;

  @Override
  public void afterPropertiesSet() throws Exception {
    APP_ID = appid;
    PARTNER = partner;
    PARTNER_KEY = partnerkey;
    NOTIFY_URL = notifyurl;
  }
}
