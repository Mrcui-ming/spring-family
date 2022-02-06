package com.atguigu.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectData {
  /**
   * excel表格格式
   * 一级分类  | 二级分类
   * 前端开发  | vue
   * 前端开发  | react
   * 后端开发  | java
   * 后端开发  | python
   * 数据库   | mysql
   *
   * 所以定义俩列表头就可以了
   */

  @ExcelProperty(index = 0)
  private String oneSubjectName;

  @ExcelProperty(index = 1)
  private String twoSubjectName;

}
