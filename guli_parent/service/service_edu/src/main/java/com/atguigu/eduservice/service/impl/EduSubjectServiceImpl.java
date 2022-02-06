package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-01-21
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

  @Override
  public void saveSubject(MultipartFile file) {
    try{

    //文件输入流
    InputStream in = file.getInputStream();
    //调用方法进行读取 这个方法可以传递流 可以串file文件 this为service对象
    EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(this)).sheet().doRead();

    }catch(Exception e){
      e.printStackTrace();
    }
  }

  @Override
  public List<OneSubject> getAllOneTwoSubject() {
    //  获取所有一级分类
    QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
    wrapperOne.eq("parent_id","0");
    List<EduSubject> oneSubjectList =  this.list(wrapperOne);

    //  获取所有二级分类
    QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
    wrapperTwo.ne("parent_id","0");
    List<EduSubject> TwoSubjectList =  this.list(wrapperTwo);

    //  获取整合的tree结构的数据 [{id,title},{id,title}] -> [{id,title,chlidren},{id,title,chlidren}]
    List<OneSubject> finalOneSubjectList = new ArrayList<>();
    for (int i = 0; i < oneSubjectList.size(); i++) {

      //  把一级分类列表中的id 和 title属性设置成功
      OneSubject oneSubject = new OneSubject();
      BeanUtils.copyProperties(oneSubjectList.get(i),oneSubject);
      finalOneSubjectList.add(oneSubject);

      //  分装一级分类列表中的chlidren属性
      //  1.拿着二级分类数组的每个value 和 一级分类数组的某一个value进行id比较 如果相同就把这个二级分类放入新的二级分类数组中
      //  2.最后把新的二级分类数组 设置到 当前一级分类的chliren属性中
      List<TwoSubject> finalTwoSubjectList = new ArrayList<>();
      for (int j = 0; j < TwoSubjectList.size(); j++) {
        if( (TwoSubjectList.get(j).getParentId()).equals( oneSubjectList.get(i).getId() ) ){
          TwoSubject twoSubject = new TwoSubject();
          BeanUtils.copyProperties(TwoSubjectList.get(j),twoSubject);
          finalTwoSubjectList.add(twoSubject);
        }
      }
      oneSubject.setChildren(finalTwoSubjectList);
    }

    return finalOneSubjectList;
  }

}
