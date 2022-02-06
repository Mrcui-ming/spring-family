package com.atguigu.eduservice.entity.vo;

import lombok.Data;

@Data
public class CoursePublishVo {

  private String id;  //课程id
  private String title; //课程名称
  private String cover; //讲师头像
  private Integer lessonNum;  //总销量
  private String subjectLevelOne; //一级分类名称
  private String subjectLevelTwo; //二级分类名称
  private String teacherName;  //讲师名称
  private String price; //课程价格
}
