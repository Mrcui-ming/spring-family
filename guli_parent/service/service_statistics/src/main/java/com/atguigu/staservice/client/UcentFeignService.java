package com.atguigu.staservice.client;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.impl.UcentFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-ucenter",fallback = UcentFeignServiceImpl.class) //  这个地方对应的是集群的名称
public interface UcentFeignService {

  //  根据时间查询当天注册的人数
  @GetMapping("/educenter/member/getMemberCount/{day}")
  public R getMemberCount(@PathVariable("day") String day);

}
