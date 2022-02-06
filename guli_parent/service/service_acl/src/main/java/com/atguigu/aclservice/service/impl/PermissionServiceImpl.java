package com.atguigu.aclservice.service.impl;

import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.entity.RolePermission;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.entity.UserRole;
import com.atguigu.aclservice.mapper.PermissionMapper;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.aclservice.service.RolePermissionService;
import com.atguigu.aclservice.service.UserRoleService;
import com.atguigu.aclservice.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    //根据角色获取菜单
    @Override
    public List<Permission> selectAllMenu(List<UserRole> userRoles) {
        List<String> permissionIds = rolePermissionService.getPermissionIds(userRoles);

        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> proList = baseMapper.selectList(wrapper);

        List<Permission> baseList = new ArrayList<>();
        for(String p : permissionIds){
            List<Permission> rempList = getBaseListByID(proList,p);
            baseList.addAll(rempList);
        }

        return baseList;
    }

    //  基于permissionID获取最终的返回数据
    public static List<Permission> getBaseListByID(List<Permission> proList,String permissionId) {
        //  创建返回的list集合
        List<Permission> baseList = new ArrayList<>();
        for(Permission p : proList){
            if(permissionId.equals(p.getId())){
                p.setLevel(1);
                baseList.add(selectList(p,proList));
            }
        }
        return baseList;
    }

    //递归 获取全部菜单(3步)
    @Override
    public List<Permission> queryAllMenuGuli() {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> proList = baseMapper.selectList(wrapper);

        List<Permission> baseList = getBaseList(proList);
        return baseList;
    }
    //获取最终的返回数据(这俩个方法做递归)
    public static List<Permission> getBaseList(List<Permission> proList) {
        //  创建返回的list集合
        List<Permission> baseList = new ArrayList<>();

        for(Permission p : proList){
            if("0".equals(p.getPid())){
                p.setLevel(1);
                baseList.add(selectList(p,proList));
            }
        }
        return baseList;
    }
    //查询所有的子菜单 将子菜单添加到父菜单的children属性中
    public static Permission selectList(Permission permission,List<Permission> proList) {
        //  初始化该对象的children属性 方便添加
        permission.setChildren(new ArrayList<Permission>());

        for(Permission p : proList){
            if(permission.getId().equals(p.getPid())){
                p.setLevel(permission.getLevel() + 1);
                if(permission.getChildren() == null){
                    permission.setChildren(new ArrayList<Permission>());
                }
                permission.getChildren().add(selectList(p,proList));
            }
        }
        return permission;
    }

    //递归删除菜单(2步)
    @Override
    public void removeChildByIdGuli(String id) {
        List<String> baseList = new ArrayList<>();
        buildBaseList(id,baseList);
        baseList.add(id);
        baseMapper.deleteBatchIds(baseList);
    }
    //封装需要删除的id集合(递归)
    public void buildBaseList(String id, List<String> baseList) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("pid",id);
        wrapper.select("id");
        List<Permission> proList = baseMapper.selectList(wrapper);
        proList.stream().forEach(item -> {
            baseList.add(item.getId());
            buildBaseList(item.getId(),baseList);
        });
    }

    //给角色分配菜单
    @Override
    public void saveRolePermissionRealtionShipGuli(String roleId, String[] permissionIds) {

        List<RolePermission> rolePermissionList = new ArrayList<>();

        /**
         * 组成每组 roleId   permissionIds  这样的数据
         *           112          212
         *           221          222
         * */
        for(int i = 0; i < permissionIds.length; i++){
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionIds[i]);
            rolePermissionList.add(rolePermission);
        }

        rolePermissionService.saveBatch(rolePermissionList);
    }

    //根据用户查询权限
    @Override
    public List<String> getAuthList(String principal) {

        User user = userService.getOne(new QueryWrapper<User>().eq("username",principal));

        //  查出所有的角色
        List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id",user.getId()));

        //  查出所有的权限
        List<String> permissionIds = rolePermissionService.getPermissionIds(userRoles);

        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> proList = baseMapper.selectList(wrapper);

        List<String> authList = new ArrayList<>();
        for(String pid:permissionIds){
            List<String> rempList = getAuthListInfo(proList,pid);
            authList.addAll(rempList);
        }

        return authList;
    }
    //获取全部的权限
    @Override
    public List<String> getAllAuthList() {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("pid",1);
        wrapper.select("id");
        List<Permission> proList = baseMapper.selectList(wrapper);
        List<String> permissionIds = proList.stream().map(c->c.getId()).collect(Collectors.toList());

        QueryWrapper<Permission> pwrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> PermissionList = baseMapper.selectList(pwrapper);

        List<String> authList = new ArrayList<>();
        for(String pid:permissionIds){
            List<String> rempList = getAuthListInfo(PermissionList,pid);
            authList.addAll(rempList);
        }

        return authList;
    }

    //获取用户权限(这俩个方法做递归)
    public List<String> getAuthListInfo(List<Permission> proList,String pid) {
        List<Permission> authList = new ArrayList<>();

        for(Permission p : proList){
            if(pid.equals(p.getId())){
                authList.add(p);
                authList.addAll(getAuthListInfoDg(pid,proList));
            }
        }

        List<String> baseList = new ArrayList<>();
        for(Permission p: authList){
            if(p.getPermissionValue()!=null){
                baseList.add(p.getPermissionValue());
            }
        }
        return baseList;
    }
    //获取用户权限
    public List<Permission> getAuthListInfoDg(String id,List<Permission> proList) {
        List<Permission> authList = new ArrayList<>();
        for(Permission p : proList){
            if(id.equals(p.getPid())){
                authList.add(p);
                authList.addAll(getAuthListInfoDg(p.getId(),proList));
            }
        }
        return authList;
    }



}
