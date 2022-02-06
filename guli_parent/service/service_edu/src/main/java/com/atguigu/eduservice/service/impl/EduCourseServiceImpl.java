package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-01-22
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

  @Autowired
  private EduCourseDescriptionService descriptionService;

  @Autowired
  private EduVideoService videoService;

  @Autowired
  private EduChapterService chapterService;

  @Override
  public String saveCourseInfo(CourseInfoVo courseInfoVo) {

    //  添加记录到edu_course课程表 逐渐策略实在new出主键所在类的时候 这个主键值就被创建出来了
    EduCourse course = new EduCourse();
    BeanUtils.copyProperties(courseInfoVo,course);
    this.save(course);

    //  获取主键id 为了实现一对一 主键唯一
    String courseId = course.getId();

    //  添加记录到edu_course_description课程简介表
    EduCourseDescription courseDescription = new EduCourseDescription();
    BeanUtils.copyProperties(courseInfoVo,courseDescription);
    courseDescription.setId(courseId);
    descriptionService.save(courseDescription);

    return courseId;
  }

  @Override
  public CourseInfoVo publishCourseInfo(String couseId) {

    //  创建返回值
    CourseInfoVo courseInfoVo = new CourseInfoVo();

    //  获取课程表信息edu_course
    EduCourse course = this.getById(couseId);
    BeanUtils.copyProperties(course,courseInfoVo);

    //  获取课程表信息edu_course_description
    EduCourseDescription courseDescription = descriptionService.getById(couseId);
    courseInfoVo.setDescription(courseDescription.getDescription());

    return courseInfoVo;
  }

  @Override
  public String updateCourseInfo(CourseInfoVo courseInfoVo) {

    EduCourse course = new EduCourse();
    BeanUtils.copyProperties(courseInfoVo,course);

    //  更新课程表edu_course
    this.updateById(course);

    //  更新课程简介表edu_course
    EduCourseDescription courseDescription = new EduCourseDescription();
    courseDescription.setId(courseInfoVo.getId());
    courseDescription.setDescription(courseInfoVo.getDescription());
    descriptionService.updateById(courseDescription);

    return courseInfoVo.getId();
  }

  @Override
  public CoursePublishVo getPublishCourseInfoById(String courseId) {
    return baseMapper.getPublishCourseInfo(courseId);
  }

  @Override
  public boolean deleteCourseAny(String courseId) {
    int res;
    //  1. 删除课程的同时 需要删除章节 小节 简介
    //  2. 由于表于表之间是有外键约束的 所以删除的时候需要根据一个 先删一对多 处于多的那张表

    //  删除小节
    videoService.deleteVideoByCourseId(courseId);

    //  删除章节
    chapterService.deleteChapterByCourseId(courseId);

    //  删除简介
    descriptionService.removeById(courseId);

    //  删除课程
    res = baseMapper.deleteById(courseId);

    return res > 0; //自动类型转换
  }

  @Override
  @Cacheable(value = "course",key = "'selectIndexList'")
  public List<EduCourse> getCourseList() {

    QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
    courseWrapper.last("limit 0,8");
    courseWrapper.orderByDesc("id");

    return baseMapper.selectList(courseWrapper);
  }

  @Override
  public Map<String, Object> getFrontCourseList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo) {

    QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();

    String title = courseFrontVo.getTitle();
    String teacherId = courseFrontVo.getTeacherId();
    String subjectParentId = courseFrontVo.getSubjectParentId();
    String subjectId = courseFrontVo.getSubjectId();
    String buyCountSort = courseFrontVo.getBuyCountSort();
    String gmtCreateSort = courseFrontVo.getGmtCreateSort();
    String priceSort = courseFrontVo.getPriceSort();

    if(!StringUtils.isEmpty(title)){
      courseWrapper.like("title",title);
    }
    if(!StringUtils.isEmpty(teacherId)){
      courseWrapper.eq("teacher_id",teacherId);
    }
    if(!StringUtils.isEmpty(subjectParentId)){
      courseWrapper.eq("subject_parent_id",subjectParentId);
    }
    if(!StringUtils.isEmpty(subjectId)){
      courseWrapper.eq("subject_id",subjectId);
    }
    if(!StringUtils.isEmpty(buyCountSort)){
      courseWrapper.orderByDesc("buy_count");
    }
    if(!StringUtils.isEmpty(gmtCreateSort)){
      courseWrapper.orderByDesc("gmt_create");
    }
    if(!StringUtils.isEmpty(priceSort)){
      courseWrapper.orderByDesc("price");
    }

    baseMapper.selectPage(coursePage,courseWrapper);

    List<EduCourse> records = coursePage.getRecords();
    long current = coursePage.getCurrent();
    long pages = coursePage.getPages();
    long size = coursePage.getSize();
    long total = coursePage.getTotal();
    boolean hasNext = coursePage.hasNext();//下一页
    boolean hasPrevious = coursePage.hasPrevious();//上一页

    Map<String,Object> dataMap = new HashMap<>();
    dataMap.put("items", records);
    dataMap.put("current", current);
    dataMap.put("pages", pages);
    dataMap.put("size", size);
    dataMap.put("total", total);
    dataMap.put("hasNext", hasNext);
    dataMap.put("hasPrevious", hasPrevious);

    return dataMap;
  }

  @Override
  public CourseWebVo getBaseCourseInfo(String courseId) {
    return baseMapper.getBaseCourseInfo(courseId);
  }

}
