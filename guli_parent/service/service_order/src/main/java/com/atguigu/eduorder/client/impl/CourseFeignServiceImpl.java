package com.atguigu.eduorder.client.impl;

import com.atguigu.commonutils.couseVo.CourseFeignVo;
import com.atguigu.eduorder.client.CourseFeignService;
import org.springframework.stereotype.Component;

@Component
public class CourseFeignServiceImpl implements CourseFeignService {

  @Override
  public CourseFeignVo getCourseInfoOrder(String id) {
    CourseFeignVo vo = new CourseFeignVo();
    vo.setCode(20001);
    vo.setMessage("服务器繁忙,请稍后再试");
    return vo;
  }

}
