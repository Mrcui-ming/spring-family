package com.atguigu.staservice.client.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcentFeignService;
import org.springframework.stereotype.Component;

@Component
public class UcentFeignServiceImpl implements UcentFeignService {

  @Override
  public R getMemberCount(String day) {
    return R.error().message("服务器繁忙,请稍后再试");
  }

}
