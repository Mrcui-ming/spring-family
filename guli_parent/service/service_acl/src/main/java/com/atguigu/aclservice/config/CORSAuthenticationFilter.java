package com.atguigu.aclservice.config;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CORSAuthenticationFilter extends FormAuthenticationFilter {

  /**
   * 直接过滤可以访问的请求类型
   */
  private static final String REQUET_TYPE = "OPTIONS";


  public CORSAuthenticationFilter() {
    super();
  }


  @Override
  public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    System.out.println(((HttpServletRequest) request).getMethod().toUpperCase());
    if (((HttpServletRequest) request).getMethod().toUpperCase().equals(REQUET_TYPE)) {
      return true;
    }
    return super.isAccessAllowed(request, response, mappedValue);
  }

}