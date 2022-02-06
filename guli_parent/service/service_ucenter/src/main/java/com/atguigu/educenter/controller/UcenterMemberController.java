package com.atguigu.educenter.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
  登录 注册
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {

  @Autowired
  private UcenterMemberService memberService;

  //  登录
  @PostMapping("/login")
  public R login(@RequestBody UcenterMember member) {
    String token = memberService.login(member);
    return R.ok().data("token",token);
  }

  //  注册
  @PostMapping("/register")
  public R register(@RequestBody RegisterVo registerVo) {
    memberService.register(registerVo);
    return R.ok();
  }

  //  从请求头中获取token
  @GetMapping("/getMemberInfo")
  public R getMemberInfo(HttpServletRequest request) {
    UcenterMember member = memberService.getMemberInfo(request);
    return R.ok().data("item",member);
  }

  //  从请求头中获取token
  @GetMapping("/getMemberInfoByToken")
  public R getMemberInfoByToken(@RequestParam(value = "token") String token) {
    UcenterMember member = memberService.getMemberInfoByToken(token);
    return R.ok().data("item",member);
  }

  //  根据时间查询当天注册的人数
  @GetMapping("/getMemberCount/{day}")
  public R getMemberCount(@PathVariable("day") String day) {
    int registerCount = memberService.getMemberCount(day);
    return R.ok().data("registerCount",registerCount);
  }

}

