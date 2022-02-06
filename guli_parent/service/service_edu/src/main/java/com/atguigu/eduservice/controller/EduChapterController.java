package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-01-22
 */
@RestController
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {

  @Autowired
  private EduChapterService chapterService;

  //  根据课程id 封装章节小节的数据结构
  @GetMapping("/getChapterVideo/{id}")
  public R getChapterVideo(@PathVariable("id") String courseId) {
    List<ChapterVo> chapterVideoList =  chapterService.getChapterVideoByCourseId(courseId);
    return R.ok().data("items",chapterVideoList);
  }

  //  根据章节id 查询章节 用于数据回显
  @GetMapping("/getChapterInfo/{chapterId}")
  public R getChapterInfo(@PathVariable("chapterId") String chapterId) {
    EduChapter chapter =  chapterService.getById(chapterId);
    return R.ok().data("item",chapter);
  }

  //  添加章节
  @PostMapping("/addChapter")
  public R addChapter(@RequestBody EduChapter chapter) {
    chapterService.save(chapter);
    return R.ok();
  }

  //  更新章节
  @PostMapping("/updateChapter")
  public R updateChapter(@RequestBody EduChapter chapter) {
    chapterService.updateById(chapter);
    return R.ok();
  }

  //  删除章节
  //  删除章节有俩种做法 1.删除章节 章节之下的所有小节也被删除 2.如果章节之下有小节 那么就不允许删除章节 我们选用第2种
  @DeleteMapping("/{chapterId}")
  public R deleteChapter(@PathVariable("chapterId") String chapterId) {
    boolean res = chapterService.deleteChapter(chapterId);
    if(res){
      return R.ok();
    }else{
      return R.error();
    }
  }

}

