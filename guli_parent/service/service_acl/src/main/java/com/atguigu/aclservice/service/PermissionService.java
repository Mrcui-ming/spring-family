package com.atguigu.aclservice.service;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface PermissionService extends IService<Permission> {

    //根据角色获取菜单
    List<Permission> selectAllMenu(List<UserRole> userRoles);

    //获取全部菜单
    List<Permission> queryAllMenuGuli();

    //递归删除菜单
    void removeChildByIdGuli(String id);

    //给角色分配权限
    void saveRolePermissionRealtionShipGuli(String roleId, String[] permissionId);

    //获取用户权限
    List<String> getAuthList(String principal);

    //获取所有的权限
    List<String> getAllAuthList();
}
