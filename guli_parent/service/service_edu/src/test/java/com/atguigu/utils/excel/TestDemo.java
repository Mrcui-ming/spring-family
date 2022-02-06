package com.atguigu.utils.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class TestDemo {

  //  设置excel的列名称
  @ExcelProperty(value = "学生学号",index = 0)
  private String sno;

  @ExcelProperty(value = "学生姓名",index = 1)
  private String sname;

}
