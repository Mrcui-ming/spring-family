package com.test;

import com.atguigu.commonutils.Md5Utils;

public class TestMd5 {

  public static void main(String[] args) {

    String token = Md5Utils.encrypt("123");
    System.out.println(token);
  }
}
