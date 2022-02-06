package com.atguigu.eduservice.client.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodFeignService;
import com.netflix.client.ClientException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFeignServiceImpl implements VodFeignService {

  @Override
  public R removeAlyVideo(String id) throws ClientException {
    return R.error().message("对方服务器繁忙,请稍后再试");
  }

  @Override
  public R removeMoreAlyVideo(List<String> videoIdList) {
    return R.error().message("对方服务器繁忙,请稍后再试");
  }


}
