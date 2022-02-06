package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-01-22
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

  @Autowired
  private EduVideoService videoService;

  @Override
  public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

    //  获取所有关联当前课程id的章节
    QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
    chapterWrapper.eq("course_id",courseId);
    List<EduChapter> chapterList = this.list(chapterWrapper);

    //  获取所有关联当前课程id的小姐
    QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
    videoWrapper.eq("course_id",courseId);
    List<EduVideo> videoList = videoService.list(videoWrapper);

    //  分装最终的数据结构
    List<ChapterVo> finalList = new ArrayList<>();
    for(int i = 0; i < chapterList.size(); i++){
      ChapterVo chapterVo = new ChapterVo();
      BeanUtils.copyProperties(chapterList.get(i),chapterVo);
      finalList.add(chapterVo);

      List<VideoVo> videoVoList = new ArrayList<>();
      for(int j = 0; j < videoList.size(); j ++){
        if((videoList.get(j).getChapterId()).equals(chapterList.get(i).getId())){
          VideoVo videoVo = new VideoVo();
          BeanUtils.copyProperties(videoList.get(j),videoVo);
          videoVoList.add(videoVo);
        }
      }
      chapterVo.setChildren(videoVoList);
    }
    return finalList;
  }

  @Override
  public boolean deleteChapter(String chapterId) {
    //  判断是否章节内是否存在小节
    QueryWrapper<EduVideo> chapterWrapper = new QueryWrapper<>();
    chapterWrapper.eq("chapter_id",chapterId);
    int count = videoService.count(chapterWrapper);
    int res;

    if(count > 0){
      throw new GuliException(20001,"不具备删除该章节的条件");
    }else {
      res = baseMapper.deleteById(chapterId);
    }

    //  自动类型转化
    return res > 0;
  }

  @Override
  public void deleteChapterByCourseId(String courseId) {
    QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
    chapterWrapper.eq("course_id",courseId);
    baseMapper.delete(chapterWrapper);
  }

}
