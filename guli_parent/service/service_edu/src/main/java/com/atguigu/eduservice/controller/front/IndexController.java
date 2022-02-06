package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eduservice/index")
@CrossOrigin
public class IndexController {

  @Autowired
  private EduTeacherService teacherService;

  @Autowired
  private EduCourseService courseService;

  @GetMapping("/getCourseTeacherList")
  public R getCourseTeahcerList() {

    List<EduCourse> courseList = courseService.getCourseList();
    List<EduTeacher> teacherList = teacherService.getTeahcerList();

    return R.ok().data("teahcerItems",teacherList).data("courseItems",courseList);
  }

}
