package com.atguigu.eduorder.client.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduorder.client.UcentFeignService;
import org.springframework.stereotype.Component;

@Component
public class UcentFeignServiceImpl implements UcentFeignService {

  @Override
  public R getMemberInfoByToken(String token) {
    return R.error().message("服务器繁忙,请稍后再试");
  }

}
