package com.atguigu.eduservice.client.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.CommentFeignService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CommentFeignServiceImpl implements CommentFeignService {

  @Override
  public R getMemberInfo(HttpServletRequest request) {
    return R.error().message("对方服务器繁忙,请稍后再试");
  }

  @Override
  public R getMemberInfoByToken(String token) {
    return R.error().message("对方服务器繁忙,请稍后再试");
  }

}
