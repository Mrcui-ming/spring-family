package com.atguigu.utils.excel;
import com.alibaba.excel.util.StringUtils;
import org.junit.Test;

import java.util.Arrays;

public class test {
  public static void main(String[] args) {
    aaa a = new aaa();
    System.out.println((String)a.getId()=="");
  }

}
class aaa{
  private String id = "";
  public String getId() {
    return id;
  }
}
