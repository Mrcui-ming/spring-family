package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-01-18
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

  @Override
  @Cacheable(value = "teacher",key = "'selectIndexList'")
  public List<EduTeacher> getTeahcerList() {

    QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
    teacherWrapper.last("limit 0,4");
    teacherWrapper.orderByDesc("id");

    return baseMapper.selectList(teacherWrapper);
  }

  @Override
  public Map<String, Object> getTeacherFrontList(Page teacherPage) {
    QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
    teacherWrapper.orderByDesc("id");
    //把分页数据封装到pageTeacher对象
    baseMapper.selectPage(teacherPage,teacherWrapper);

    List<EduTeacher> records = teacherPage.getRecords();
    long current = teacherPage.getCurrent();
    long pages = teacherPage.getPages();
    long size = teacherPage.getSize();
    long total = teacherPage.getTotal();
    boolean hasNext = teacherPage.hasNext();//下一页
    boolean hasPrevious = teacherPage.hasPrevious();//上一页

    //把分页数据获取出来，放到map集合
    Map<String, Object> map = new HashMap<>();
    map.put("items", records);
    map.put("current", current);
    map.put("pages", pages);
    map.put("size", size);
    map.put("total", total);
    map.put("hasNext", hasNext);
    map.put("hasPrevious", hasPrevious);

    return map;
  }
}
