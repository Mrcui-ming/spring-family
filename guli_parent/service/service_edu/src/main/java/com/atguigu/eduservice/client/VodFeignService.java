package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.impl.VodFeignServiceImpl;
import com.netflix.client.ClientException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "service-vod",fallback = VodFeignServiceImpl.class) //  这个地方对应的是集群的名称
public interface VodFeignService {

  //  删除视频
  @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
  public R removeAlyVideo(@PathVariable("id") String id) throws ClientException;

  //  批量删除视频
  @DeleteMapping("/eduvod/video/removeMoreAlyVideo")
  public R removeMoreAlyVideo(@RequestParam("videoIdList") List<String> videoIdList);

}
