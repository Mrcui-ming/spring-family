package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.CommentFeignService;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-02-03
 */
@RestController
@RequestMapping("/eduservice/comment")
@CrossOrigin
public class EduCommentController {

  @Autowired
  private CommentFeignService commentFeignService;

  @Autowired
  private EduCommentService commentService;

  @GetMapping("/getUcentInfo")
  public R getUcentInfo(HttpServletRequest request) {
    return commentFeignService.getMemberInfoByToken(request.getHeader("token"));
  }

  @PostMapping("/commitComment")
  public R commitcomment(@RequestBody EduComment comment) {
    commentService.save(comment);
    return R.ok();
  }

  @GetMapping("/getComment/{page}/{limit}/{courseId}")
  public R getComment(@PathVariable("page") Long page,
                      @PathVariable("limit") Long limit,
                      @PathVariable("courseId") String courseId){
    Page<EduComment> commentPage = new Page<>(page,limit);

    QueryWrapper<EduComment> commentWrapper = new QueryWrapper<>();
    commentWrapper.orderByDesc("gmt_create");
    commentWrapper.eq("course_id",courseId);

    commentService.page(commentPage,commentWrapper);

    List<EduComment> records = commentPage.getRecords();
    long current = commentPage.getCurrent();
    long pages = commentPage.getPages();
    long size = commentPage.getSize();
    long total = commentPage.getTotal();
    boolean hasNext = commentPage.hasNext();//下一页
    boolean hasPrevious = commentPage.hasPrevious();//上一页

    //把分页数据获取出来，放到map集合
    Map<String, Object> dataMap = new HashMap<>();
    dataMap.put("items", records);
    dataMap.put("current", current);
    dataMap.put("pages", pages);
    dataMap.put("size", size);
    dataMap.put("total", total);
    dataMap.put("hasNext", hasNext);
    dataMap.put("hasPrevious", hasPrevious);

    return R.ok().data(dataMap);
  }
}

