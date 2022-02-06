package com.atguigu.aclservice.config;

import com.atguigu.aclservice.entity.realm.CustomRealm;
import com.atguigu.aclservice.service.UserService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

  @Autowired
  private UserService userService;

  /**
   * 1. Filter工厂，设置对应的过滤条件和跳转条件
   * */
  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

    //权限管理realm
    shiroFilterFactoryBean.setSecurityManager(securityManager);

    //登录，未授权
    shiroFilterFactoryBean.setLoginUrl("/admin/acl/permission/login");
    shiroFilterFactoryBean.setUnauthorizedUrl("/admin/acl/permission/error");

    Map<String, String> map = new LinkedHashMap<>();
    //剩余的都拦截
    map.put("/**", "authc");

    shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

    //自定义拦截器
    Map<String, Filter> customFilterMap = new LinkedHashMap<>();
    customFilterMap.put("corsAuthenticationFilter", statelessAuthcFilter());
    shiroFilterFactoryBean.setFilters(customFilterMap);

    return shiroFilterFactoryBean;
  }

  /**
   * 2. securityManager权限管理，配置主要是Realm的管理认证
   * */
  @Bean
  public SecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    //自定义的shiro session 缓存管理器
    securityManager.setSessionManager(sessionManager(sessionDAO()));

    securityManager.setRealm(myShiroRealm());
    return securityManager;
  }

  /**
   * 3. Realm 将自己的验证方式加入容器
   * */
  @Bean
  public CustomRealm myShiroRealm() {
    CustomRealm customRealm = new CustomRealm(userService);
    //设置加密方式
    customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
    return customRealm;
  }

  /**
   * 4. 开启shiro 的AOP注解支持
   */
  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;
  }

  /**
   * 5. 自定义的 shiro session 缓存管理器，用于跨域等情况下使用 token 进行验证，不依赖于sessionId
   */
  @Bean
  public SessionManager sessionManager(EnterpriseCacheSessionDAO sessionDAO){
    //将我们继承后重写的shiro session 注册
    ShiroSession shiroSession = new ShiroSession();
    shiroSession.setSessionDAO(new EnterpriseCacheSessionDAO());
    shiroSession.setSessionDAO(sessionDAO);
    return shiroSession;
  }

  /**
   * 6. 自定义的shiro过滤器
   * */
  @Bean
  public CORSAuthenticationFilter statelessAuthcFilter() {
    return new CORSAuthenticationFilter();
  }


  @Bean(name="sessionDAO")
  public EnterpriseCacheSessionDAO sessionDAO() {
    EnterpriseCacheSessionDAO abstractSessionDAO=new EnterpriseCacheSessionDAO();
    abstractSessionDAO.setSessionIdGenerator(new UuidSessionIdGenerator());
    return abstractSessionDAO;
  }

  /**
   * 自定义md5加密规则
   * */
  @Bean
  public HashedCredentialsMatcher hashedCredentialsMatcher() {
    HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
    //指定加密方式
    credentialsMatcher.setHashAlgorithmName("MD5");
    //加密次数
    credentialsMatcher.setHashIterations(1);
    //此处的设置，true加密用的hex编码，false用的base64编码
    //credentialsMatcher.setStoredCredentialsHexEncoded(true);
    return credentialsMatcher;
  }

}