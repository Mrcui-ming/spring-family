package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.Md5Utils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-01-31
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  public String login(UcenterMember member) {

    String mobile = member.getMobile();
    String password = member.getPassword();

    if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
      throw new GuliException(20001,"用户名或密码为空");
    }

    QueryWrapper<UcenterMember> memberWrapper = new QueryWrapper<>();
    memberWrapper.eq("mobile",mobile);
    UcenterMember mobileMemter = baseMapper.selectOne(memberWrapper);

    //  判断用户是否存在
    if(mobileMemter == null){
      throw new GuliException(20001,"登录失败,用户不存在");
    }

    //  判断密码是否正确
    if(!Md5Utils.encrypt(password).equals(mobileMemter.getPassword())){
      throw new GuliException(20001,"登陆失败,密码错误");
    }

    //  判断用户是否禁用
    if(mobileMemter.getIsDisabled()) {
      throw new GuliException(20001,"登录失败,用户已被禁用");
    }

    //  生成token字符串
    String token = JwtUtils.getJwtToken(mobileMemter.getId(),mobileMemter.getNickname());

    return token;
  }

  @Override
  public void register(RegisterVo registerVo) {
    String nickname = registerVo.getNickname();
    String password = registerVo.getPassword();
    String code = registerVo.getCode();
    String mobile = registerVo.getMobile();

    if(StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password) ||
       StringUtils.isEmpty(code) || StringUtils.isEmpty(mobile)){
      throw new GuliException(20001,"注册失败");
    }

    QueryWrapper<UcenterMember> memberWrapper = new QueryWrapper<>();
    memberWrapper.eq("mobile",mobile);
    int count = baseMapper.selectCount(memberWrapper);
    if(count > 0){
      throw new GuliException(20001,"注册失败,该手机号已被注册");
    }

    String mobileCode = (String) redisTemplate.opsForValue().get(mobile);
    if(mobileCode==code){
      throw new GuliException(20001,"注册失败,验证码错误");
    }

    UcenterMember ucenterMember = new UcenterMember();
    ucenterMember.setNickname(registerVo.getNickname());
    //  md5加密
    String yPassword = Md5Utils.encrypt(registerVo.getPassword());
    ucenterMember.setPassword(yPassword);
    ucenterMember.setMobile(registerVo.getMobile());
    ucenterMember.setIsDisabled(false);
    ucenterMember.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    baseMapper.insert(ucenterMember);
  }

  @Override
  public UcenterMember getMemberInfo(HttpServletRequest request) {

    //  判断token是否正常
    Boolean res = JwtUtils.checkToken(request);
    if(res){
      String memberId = JwtUtils.getMemberIdByRequest(request);

      UcenterMember member = baseMapper.selectById(memberId);
      return member;
    }
    else{
      throw new GuliException(20001,"登录信息失效,请重新登录");
    }

  }

  @Override
  public UcenterMember getOpenIdMember(String openid) {
    QueryWrapper<UcenterMember> memberWrapper = new QueryWrapper<>();
    memberWrapper.eq("openid",openid);
    return baseMapper.selectOne(memberWrapper);
  }

  @Override
  public UcenterMember getMemberInfoByToken(String token) {
    //  判断token是否正常
    Boolean res = JwtUtils.checkToken(token);
    if(res){
      String memberId = JwtUtils.getMemberIdByJwtToken(token);

      UcenterMember member = baseMapper.selectById(memberId);
      return member;
    }
    else{
      throw new GuliException(20001,"登录信息失效,请重新登录");
    }

  }

  @Override
  public int getMemberCount(String day) {
    return baseMapper.getMemberCount(day);
  }

}
