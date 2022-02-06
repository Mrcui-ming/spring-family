package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.impl.CommentFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient(value = "service-ucenter",fallback = CommentFeignServiceImpl.class) //  这个地方对应的是集群的名称
public interface CommentFeignService {

  //  根据request获取用户信息
  @GetMapping("/educenter/member/getMemberInfo")
  public R getMemberInfo(@RequestParam(value = "request") HttpServletRequest request);

  //  根据token获取用户信息
  @GetMapping("/educenter/member/getMemberInfoByToken")
  public R getMemberInfoByToken(@RequestParam(value = "token") String token);
}
