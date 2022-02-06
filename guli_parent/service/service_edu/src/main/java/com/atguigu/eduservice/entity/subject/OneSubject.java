package com.atguigu.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OneSubject {

  private String id;

  private String title;

  //  一级分类之下的二级分类
  private List<TwoSubject> children = new ArrayList<>();
}
