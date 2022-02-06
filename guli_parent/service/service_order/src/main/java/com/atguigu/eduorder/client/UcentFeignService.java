package com.atguigu.eduorder.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "service-ucenter")
public interface UcentFeignService {

  @GetMapping("/educenter/member/getMemberInfoByToken")
  public R getMemberInfoByToken(@RequestParam(value = "token") String token);

}
