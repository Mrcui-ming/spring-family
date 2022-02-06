package com.atguigu.aclservice.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

public class UuidSessionIdGenerator implements SessionIdGenerator {

  //  自定义sessionId
  @Override
  public Serializable generateId(Session session) {

    Serializable uuid = new JavaUuidSessionIdGenerator().generateId(session);

    return uuid;
  }

}
