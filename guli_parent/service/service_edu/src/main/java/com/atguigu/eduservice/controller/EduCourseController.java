package com.atguigu.eduservice.controller;


import com.alibaba.excel.util.StringUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodFeignService;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.sun.org.apache.bcel.internal.generic.RET;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-01-22
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

  @Autowired
  private EduCourseService courseService;

  @Resource
  private VodFeignService feignService;

  @ApiOperation(value = "添加课程")
  @PostMapping("/addCourseInfo")
  public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
    String id = courseService.saveCourseInfo(courseInfoVo);
    return R.ok().data("courseId",id);
  }

  @ApiOperation(value = "根据课程id 查询课程信息")
  @GetMapping("/getPublishCourseInfo/{courseId}")
  public R getPublishCourseInfo(@PathVariable("courseId") String courseId) {
    CourseInfoVo courseInfoVo = courseService.publishCourseInfo(courseId);
    return R.ok().data("item",courseInfoVo);
  }

  @ApiOperation(value = "更新课程")
  @PostMapping("/updateCourseInfo")
  public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
    String id = courseService.updateCourseInfo(courseInfoVo);
    return R.ok().data("courseId",id);
  }

  @ApiOperation(value = "根据课程id查询课程确认信息")
  @GetMapping("/getPublishCourseInfoById/{courseId}")
  public R getPublishCourseInfoById(@PathVariable String courseId) {
    CoursePublishVo coursePublishVo = courseService.getPublishCourseInfoById(courseId);
    return R.ok().data("publishCourse",coursePublishVo);
  }

  @ApiOperation(value = "更改课程为发布状态")
  @PostMapping("/publishCourse/{courseId}")
  public R publishCourse(@PathVariable("courseId") String courseId){
    EduCourse course = new EduCourse();
    course.setId(courseId);
    course.setStatus("Normal"); //课程发布和未发布是根据status属性来决定的 Normal表示发布
    courseService.updateById(course);
    return R.ok();
  }

  @ApiOperation(value = "查询课程列表信息")
  @PostMapping("/getCourseList/{current}/{limit}")
  public R getCourseList(@PathVariable("current") Integer current,
                         @PathVariable("limit") Integer limit,
                         @RequestBody(required = false) CourseQuery courseQuery) {

    String title = courseQuery.getTitle();
    String status = courseQuery.getStatus();

    Page<EduCourse> pageCourse = new Page<>(current,limit);

    QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();

    if(!StringUtils.isEmpty(title)){
      courseWrapper.like("title",title);
    }
    if(!StringUtils.isEmpty(status)){
      courseWrapper.eq("status",status);
    }

    courseWrapper.orderByDesc("gmt_create");

    courseService.page(pageCourse,courseWrapper);

    Map<String,Object> dataMap = new HashMap<>();
    dataMap.put("total",pageCourse.getTotal());
    dataMap.put("items",pageCourse.getRecords());

    return R.ok().data(dataMap);
  }

  @ApiOperation(value = "更改课程状态")
  @PostMapping("/updateCourseStatus/{courseId}/{status}")
  public R updateCourseStatus(@PathVariable("status") String status,
                              @PathVariable("courseId") String courseId){
    EduCourse course = new EduCourse();
    course.setId(courseId);
    course.setStatus(status);
    courseService.updateById(course);
    return R.ok();
  }

  @ApiOperation(value = "删除课程")
  @DeleteMapping("/deleteCourse/{courseId}")
  public R deleteCourse(@PathVariable("courseId") String courseId){

    boolean res = courseService.deleteCourseAny(courseId);
    if(res){
      return R.ok();
    }else{
      return R.error();
    }
  }

}

