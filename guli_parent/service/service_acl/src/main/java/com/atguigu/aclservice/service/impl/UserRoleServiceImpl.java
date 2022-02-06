package com.atguigu.aclservice.service.impl;

import com.atguigu.aclservice.entity.UserRole;
import com.atguigu.aclservice.mapper.UserRoleMapper;
import com.atguigu.aclservice.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

  @Override
  public String getRoleId(String userId) {
    QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
    wrapper.eq("user_id",userId);
    wrapper.select("role_id");
    return baseMapper.selectOne(wrapper).getRoleId();
  }
}
