package com.atguigu.eduservice.client.impl;

import com.atguigu.eduservice.client.OrderFeignService;
import org.springframework.stereotype.Component;

@Component
public class OrderFeignServiceImpl implements OrderFeignService {


  @Override
  public boolean getOrderInfoByQuery(String courseId, String memberId) {
    return false;
  }
}
