package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-01-22
 */
public interface EduCourseService extends IService<EduCourse> {

  @Transactional
  String saveCourseInfo(CourseInfoVo courseInfoVo);

  CourseInfoVo publishCourseInfo(String couseId);

  @Transactional
  String updateCourseInfo(CourseInfoVo courseInfoVo);

  CoursePublishVo getPublishCourseInfoById(String courseId);

  @Transactional
  boolean deleteCourseAny(String courseId);

  List<EduCourse> getCourseList();

  Map<String, Object> getFrontCourseList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo);

  CourseWebVo getBaseCourseInfo(String courseId);
}
