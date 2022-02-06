package com.atguigu.aclservice.controller;

import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.entity.Role;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.entity.UserRole;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.aclservice.service.RoleService;
import com.atguigu.aclservice.service.UserRoleService;
import com.atguigu.aclservice.service.UserService;
import com.atguigu.commonutils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Api("登录")
@RestController
@RequestMapping("/admin/acl/permission")
public class LoginController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRoleService userRoleService;

  @Autowired
  private PermissionService permissionService;

  @Autowired
  private RoleService roleService;

  /** 登录 */
  @GetMapping("/login")
  public R login(String username, String password) {

    // 把用户名和密码封装为 UsernamePasswordToken 对象
    UsernamePasswordToken token = new UsernamePasswordToken(username, password);

    try{
      SecurityUtils.getSubject().login(token);
    }
    catch (AuthenticationException ae) {
      return R.error().message("用户名或密码错误");
    }

    System.out.println(SecurityUtils.getSubject().isAuthenticated());
    return R.ok().data("token",SecurityUtils.getSubject().getSession().getId()).message("登录成功");
  }

  /** 获取用户信息 */
  @GetMapping("/info")
  public R info(String token) {
    String username = (String) SecurityUtils.getSubject().getPrincipal();
    List<String> roles;
    if("admin".equals(username)){
      roles = new ArrayList<>();
      roles.add("admin");
    }else{
      User user = userService.getOne(new QueryWrapper<User>().eq("username",username));
      List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id",user.getId()));
      List<String> roleIds = userRoles.stream().map(c->c.getRoleId()).collect(Collectors.toList());
      List<Role> rolees = (List<Role>) roleService.listByIds(roleIds);
      roles = rolees.stream().map(c->c.getRoleName()).collect(Collectors.toList());
    }
    HashMap<String,Object> dataMap = new HashMap();
    dataMap.put("roles",roles);
    dataMap.put("name", username);
    dataMap.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");

    return R.ok().data(dataMap);
  }

  /** 获取用户权限菜单*/
  @GetMapping("/getMenu")
  public R getMenu() {
    String username = (String) SecurityUtils.getSubject().getPrincipal();
    List<Permission> baseList;
    if("admin".equals(username)){
      baseList = permissionService.queryAllMenuGuli();
    }else{
      User user = userService.getOne(new QueryWrapper<User>().eq("username",username));
      List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id",user.getId()));;
      baseList = permissionService.selectAllMenu(userRoles);
    }
    System.out.println("baseList长度:" + baseList.size());
    return R.ok().data("permissionList",baseList);
  }

  /** 退出登录 */
  @GetMapping("/logout")
  public R logout() {
    return R.ok();
  }

  /** 统一未授权操作 */
  @GetMapping("/error")
  public R error() {
    return R.error().message("您当前没有权限执行该操作");
  }

}
