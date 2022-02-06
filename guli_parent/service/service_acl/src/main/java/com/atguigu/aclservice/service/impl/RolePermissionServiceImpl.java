package com.atguigu.aclservice.service.impl;

import com.atguigu.aclservice.entity.Role;
import com.atguigu.aclservice.entity.RolePermission;
import com.atguigu.aclservice.entity.UserRole;
import com.atguigu.aclservice.mapper.RolePermissionMapper;
import com.atguigu.aclservice.service.RolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

  @Autowired
  private RolePermissionService rolePermissionService;

  @Override
  public List<String> getPermissionIds(List<UserRole> userRoles) {
    List<String> RoleList = userRoles.stream().map(c->c.getRoleId()).collect(Collectors.toList());
    List<RolePermission> baseList = new ArrayList<>();
    for (String rid: RoleList){
      QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
      wrapper.eq("role_id",rid);
      wrapper.select("permission_id");
      List<RolePermission> rempList =  rolePermissionService.list(wrapper);
      baseList.addAll(rempList);
    }
    rolePermissionService.listByIds(RoleList);
    List<String> resList = baseList.stream().map(c->c.getPermissionId()).collect(Collectors.toList());
    return resList;
  }

}
