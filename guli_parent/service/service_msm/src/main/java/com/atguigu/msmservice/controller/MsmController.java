package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {

  @Autowired
  private MsmService msmService;

  @Autowired
  @Qualifier("redisTemplate")
  private RedisTemplate redisTemplate;

  //发送短信的方法
  @GetMapping("/send/{phone}")
  public R sendMsm(@PathVariable("phone") String phone) {
    System.out.println(phone);
    //1 从redis获取验证码，如果获取到直接返回
    String code = (String) redisTemplate.opsForValue().get(phone);
    if(!StringUtils.isEmpty(code)){
      return R.ok();
    }
    code = RandomUtil.getFourBitRandom();

    boolean res = msmService.send(phone,code,"5");
    System.out.println("结果:" + res);
    if(res){
      //发送成功，把发送成功验证码放到redis里面
      redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
      return R.ok();
    }else{
      return R.error().message("发送短信失败");
    }

  }

}
