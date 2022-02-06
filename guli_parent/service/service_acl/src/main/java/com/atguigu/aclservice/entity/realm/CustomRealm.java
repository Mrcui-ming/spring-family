package com.atguigu.aclservice.entity.realm;

import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.aclservice.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//  我们说 请求先经过controller再经过realms 其中传递的token是相同的
//  请求的用户名会默认被转成md5字符串 然后和数据库存储的md5密码进行比对
//      如果相同那么 登录认证成功
//      如果不同那么 抛出AuthenticationException异常 登录认证失败

//  默认的认证策略使用的是AtLeastOneSuccessfulStrategy: 表示只要一个realms成功，那么就认为是成功的

// 本来是实现于 Realm接口的
// 但如果单纯做认证的话 继承AuthenticatingRealm类就可以了
// AuthenticatingRealm类是Realm的实现类
public class CustomRealm extends AuthorizingRealm {

  private UserService userService;

  @Autowired
  private PermissionService permissionService;

  public CustomRealm(UserService userService){
    this.userService = userService;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException{

    //  1.AuthenticationToken转换为UsernamePasswordToken
    UsernamePasswordToken up = (UsernamePasswordToken) authenticationToken;

    //  2.从UsernamePasswordToken中获取username
    String username = up.getUsername();

    //  3.调用数据库的方法，从数据库中查询username对应的用户记录
    QueryWrapper<User> userWrapper = new QueryWrapper<>();
    userWrapper.eq("nick_name",username);
    User user = userService.getOne(userWrapper);

    /**
     *   4.根据用户的情况，来构建AuthonyiocationInfo第项并返回
     *   principal       用户名
     *   credentials     密码
     *   realmName       realm对象的名称
     * */
    Object principal = username;
    Object credentials = user.getPassword();
    String realmName = getName();
    SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal,credentials,null,realmName);
    return info;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

    //  1.从PrincipaiCollection种获取登录用户的信息 无论配置多少个realm 这个principal都是只存在一个的
    String principal = (String)principalCollection.getPrimaryPrincipal();

    //  2.给当前登录用户设置角色权限 (这一步可能需要访问数据库)
    Set<String> roles = new HashSet<>();
    roles.add("user");
    if("admin".equals(principal)){
      roles.add("admin");
    }

    //  3.给当前角色设置行为权限
    Set<String> permissions = new HashSet<>();
    List<String> authList;
    if("admin".equals(principal)){
      authList = permissionService.getAllAuthList();;
    }else{
      authList = permissionService.getAuthList(principal);;
    }
    for(String auth: authList) {
      permissions.add(auth);
    }

    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    // 设置角色权限
    info.setRoles(roles);
    // 设置行为权限
    info.setStringPermissions(permissions);
    return info;
  }

}
