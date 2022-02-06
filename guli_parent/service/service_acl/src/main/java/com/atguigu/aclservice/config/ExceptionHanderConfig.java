package com.atguigu.aclservice.config;

import com.atguigu.commonutils.R;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHanderConfig {

  @ExceptionHandler(value = UnauthorizedException.class)
  /* 这个异常是处理当前用户没有权限认证去调用方法 的异常处理 */
  public R UnauthorizedExceptionHandler() {
    return R.error().message("您当前没有权限执行该操作");
  }

  /* UnauthorizedException / AuthorizationException 这俩个异常已经足够处理shiro里面的所有异常了*/
  @ExceptionHandler(value = AuthorizationException.class)
  public R ShiroExceptionHandler() {
    return R.error().message("您当前没有权限执行该操作");
  }

}
