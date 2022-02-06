package com.atguigu.educenter.service;

import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2021-01-31
 */
public interface UcenterMemberService extends IService<UcenterMember> {

  String login(UcenterMember member);

  void register(RegisterVo registerVo);

  UcenterMember getMemberInfo(HttpServletRequest request);

  UcenterMember getOpenIdMember(String openid);

  UcenterMember getMemberInfoByToken(String token);

  int getMemberCount(String day);
}
