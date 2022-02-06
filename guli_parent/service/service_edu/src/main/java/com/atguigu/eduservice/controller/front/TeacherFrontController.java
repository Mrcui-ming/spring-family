package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/teacherfront")
@CrossOrigin
public class TeacherFrontController {

  @Autowired
  private EduTeacherService teacherService;

  @Autowired
  private EduCourseService courseService;

  @GetMapping("/getTeacherFrontList/{page}/{limit}")
  public R getTeacherFrontList(@PathVariable("page") long page,
                               @PathVariable("limit") long limit) {
    Page<EduTeacher> teacherPage = new Page(page,limit);
    Map<String, Object> dataMap = teacherService.getTeacherFrontList(teacherPage);
    return R.ok().data(dataMap);
  }

  //2 讲师详情的功能
  @GetMapping("getTeacherFrontInfo/{teacherId}")
  public R getTeacherFrontInfo(@PathVariable("teacherId") String teacherId) {
    EduTeacher teacher = teacherService.getById(teacherId);
    QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
    wrapper.eq("teacher_id",teacherId);
    List<EduCourse> courseList = courseService.list(wrapper);
    return R.ok().data("teacher",teacher).data("courseList",courseList);
  }

}
