package com.atguigu.eduorder.client;

import com.atguigu.commonutils.couseVo.CourseFeignVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(value = "service-edu")
public interface CourseFeignService {

  @GetMapping("/eduservice/coursefront/getCourseInfoOrder/{id}")
  public CourseFeignVo getCourseInfoOrder(@PathVariable("id") String id);

}
