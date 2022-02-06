package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-01-21
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

  @Autowired
  private EduSubjectService subjectService;

  //  把excel的数据 存储 到数据库
  @PostMapping("/addSubject")
  public R addSubject(MultipartFile file){
    //上传过来excel文件
    subjectService.saveSubject(file);
    return R.ok();
  }

  //课程分类列表（树形）
  @GetMapping("/getAllSubject")
  public R getAllSubject() {
    //list集合泛型是一级分类
    List<OneSubject> list = subjectService.getAllOneTwoSubject();
    return R.ok().data("items",list);
  }

}

