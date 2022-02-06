package com.atguigu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.ConstantVodUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class 阿里云短信调用sdk implements MsmService {

  @Override
  public boolean send(Map<String, Object> param, String phone) {

    String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;
    String accessKeySecret = ConstantVodUtils.ACCESS_KEY_SECRET;
    System.out.println(accessKeyId);
    System.out.println(accessKeySecret);

    DefaultProfile profile =
            DefaultProfile.getProfile("cn-hangzhou",accessKeyId,accessKeySecret);
    IAcsClient client = new DefaultAcsClient(profile);

    //  设置固定的参数配置
    CommonRequest request = new CommonRequest();
    request.setSysMethod(MethodType.POST);  //请求方法
    request.setSysDomain("dysmsapi.aliyuncs.com");  //请求所在地址
    request.setSysVersion("2017-05-25");  //初始化时间
    request.setSysAction("SendSms"); //表示发送短信服务
    request.putQueryParameter("RegionId", "cn-hangzhou");

    //  设置发送相关的参数
    request.putQueryParameter("PhoneNumbers",phone); //手机号
    request.putQueryParameter("SignName","82D7F664-CD80-4A96-A2E9-A65F0BEACB2D"); //申请阿里云 签名名称
    request.putQueryParameter("TemplateCode","SMS_211025334"); //申请阿里云 模板code
    request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param)); //验证码数据，转换json数据传递

    try{
      CommonResponse response = client.getCommonResponse(request);
      boolean success = response.getHttpResponse().isSuccess(); // 判断是否发送成功
      System.out.println(response.getData());
      return success;
    }catch(Exception e){
      e.printStackTrace();
      return false;
    }

  }

}
