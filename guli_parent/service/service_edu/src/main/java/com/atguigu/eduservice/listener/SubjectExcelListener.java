package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 这个类不能交给spring去管理
 * 我们在Excel.read()方法那里便可以看出 是new SubjectExcelListener()传入的
 *
 * 问题1:但是我们这个类 是需要做数据库的操作的 需要service
 * 会发现 没有交给spring管理的类 没办法注入javaBean 也没办法 在别的类中注入当前类 怎么解决？
 * 解决:通过构造方法传入即可
 *
 * 问题2:由于需要存到数据库当中 所以就要求不能数据不可重复
 *  excel表格格式
 *  一级分类     |   二级分类
 *  前端开发!!!  |   vue
 *  前端开发!!!  |   react
 *  后端开发!!!  |   java
 *  后端开发!!!  |   python
 *  数据库       |   mysql!!!
 *  数据库       |   mysql!!!
 * 解决:需要经过判断数据库的数据是否存在 再去存储
 * */
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

  private EduSubjectService subjectService;

  public SubjectExcelListener() {
  }
  public SubjectExcelListener(EduSubjectService subjectService) {
    this.subjectService = subjectService;
  }

  //  读取每一行数据
  @Override
  public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
    //  如果excel表格为空 那么直接退出
    if(subjectData == null){
      throw new GuliException(20001,"文件数据为空");
    }

    //  一级分类列表添加操作
    EduSubject eduOneSubject = this.existOneSubject(subjectService,subjectData.getOneSubjectName());
    if(eduOneSubject == null){
      eduOneSubject = new EduSubject();
      eduOneSubject.setParentId("0");
      eduOneSubject.setTitle(subjectData.getOneSubjectName());
      subjectService.save(eduOneSubject);
    }

    //  二级分类列表添加操作
    String pid = eduOneSubject.getId();
    EduSubject eduTwoSubject = this.existTwoSubject(subjectService,subjectData.getTwoSubjectName(),pid);
    if(eduTwoSubject == null){
      eduTwoSubject = new EduSubject();
      eduTwoSubject.setParentId(pid);
      eduTwoSubject.setTitle(subjectData.getTwoSubjectName());
      subjectService.save(eduTwoSubject);
    }

  }

  //  读取数据完成
  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {

  }

  //  判断一级分类是否存在该数据
  public EduSubject existOneSubject(EduSubjectService subjectService,String name){
    //select * from edu_subject where parent_id = 0 and title = name
    QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
    wrapper.eq("parent_id","0");
    wrapper.eq("title",name);
    EduSubject eduSubject = subjectService.getOne(wrapper);
    return eduSubject;
  }

  //  判断二级分类是否存在该数据
  public EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
    //select * from edu_subject where parent_id = pid and title = name
    QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
    wrapper.eq("parent_id",pid);
    wrapper.eq("title",name);
    EduSubject eduSubject = subjectService.getOne(wrapper);
    return eduSubject;
  }

}
