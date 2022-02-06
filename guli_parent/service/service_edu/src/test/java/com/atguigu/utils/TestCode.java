package com.atguigu.utils;

import com.atguigu.eduservice.entity.excel.SubjectData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCode {

  @Test
  public void test1() {
    List<SubjectData> lists1 = new ArrayList<>();
    SubjectData subjectData = new SubjectData();
    subjectData.setOneSubjectName("1");
    subjectData.setTwoSubjectName("2");
    lists1.add(subjectData);
    lists1.add(subjectData);
    lists1.add(subjectData);
    List<SubjectData> lists2 = new ArrayList<>();
    lists2.addAll(lists1);
    for(SubjectData s: lists2){
      System.out.println(s);
    }
  }
}
