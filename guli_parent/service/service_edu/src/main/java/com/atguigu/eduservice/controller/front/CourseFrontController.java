package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.couseVo.CourseFeignVo;
import com.atguigu.eduservice.client.OrderFeignService;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {

  @Autowired
  private EduCourseService courseService;

  @Autowired
  private EduChapterService chapterService;

  @Autowired
  private OrderFeignService orderFeignService;

  @PostMapping("getFrontCourseList/{page}/{limit}")
  public R getFrontCourseList(@PathVariable("page") Long page,
                              @PathVariable("limit") Long limit,
                              @RequestBody(required = false) CourseFrontVo courseFrontVo) {
    Page<EduCourse> coursePage = new Page<>(page,limit);
    Map<String, Object> dataMap = courseService.getFrontCourseList(coursePage, courseFrontVo);
    return R.ok().data(dataMap);
  }

  //  课程详情的方法
  @GetMapping("/getFrontCourseInfo/{courseId}")
  public R getFrontCourseInfo(@PathVariable("courseId") String courseId,
                              HttpServletRequest request) {

    //  获取课程信息 课程描述 讲师信息 分类信息
    CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);

    //  获取章节小节信息
    List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

    //  查询该用户是否购买了此课程 判断是因为未登录的话就不必要查询了
    boolean isBuy = false;
    if(JwtUtils.getMemberIdByRequest(request)!=""){
      isBuy = orderFeignService.getOrderInfoByQuery(courseId, JwtUtils.getMemberIdByRequest(request));
    }

    return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",isBuy);
  }

  //  根据课程id查询课程信息
  @GetMapping("/getCourseInfoOrder/{id}")
  public CourseFeignVo getCourseInfoOrder(@PathVariable("id") String id) {
    CourseWebVo courseWebVo = courseService.getBaseCourseInfo(id);
    CourseFeignVo courseFeignVo = new CourseFeignVo();
    BeanUtils.copyProperties(courseWebVo,courseFeignVo);
    return courseFeignVo;
  }

}
