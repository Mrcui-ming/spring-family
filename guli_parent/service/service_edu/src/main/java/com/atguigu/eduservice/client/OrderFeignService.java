package com.atguigu.eduservice.client;

import com.atguigu.eduservice.client.impl.OrderFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-order",fallback = OrderFeignServiceImpl.class) //  这个地方对应的是集群的名称
public interface OrderFeignService {

  @GetMapping("/eduorder/order/getOrderInfoByQuery/{courseId}/{memberId}")
  public boolean getOrderInfoByQuery(@PathVariable("courseId") String courseId,
                                     @PathVariable("memberId") String memberId);

}
