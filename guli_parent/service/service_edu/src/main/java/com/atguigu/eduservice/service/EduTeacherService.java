package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-01-18
 */
public interface EduTeacherService extends IService<EduTeacher> {

  List<EduTeacher> getTeahcerList();

  Map<String, Object> getTeacherFrontList(Page teacherPage);
}
