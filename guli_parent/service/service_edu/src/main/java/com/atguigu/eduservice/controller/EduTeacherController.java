package com.atguigu.eduservice.controller;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

/**
 * @Api
 * @ApiOperation
 * @ApiParam
 * 以上三个注解对开发没有影响，知识起到了提示的作用(swagger-ui.html中可看到效果)
 *
 * service默认的mp方法和mapper默认的mp方法不同 主要体现在 查询/删除上
 * service没有select  mapper有select
 * service是delete   mapper是remove
 */
@Api(description = "讲师管理")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {

  @Autowired
  private EduTeacherService eduTeacherService;

  @ApiOperation(value = "查询老师列表")
  @GetMapping("/findAll")
  public R findTeachers() {
    List list = eduTeacherService.list(null);
    HashMap dataMap = new HashMap();
    dataMap.put("items",list);
    return R.ok().data(dataMap);
  }

  @ApiOperation(value = "逻辑删除讲师")
  @DeleteMapping("/{id}")
  public R removeTeacher(@ApiParam(name = "id",value = "讲师id",required = true)
                         @PathVariable("id") String id) {
    boolean res = eduTeacherService.removeById(id);
    if(res){
      return R.ok();
    }else{
      return R.error();
    }

  }

  @ApiOperation(value = "分页查询")
  @GetMapping("/pageTeacher/{current}/{limit}")
  public R pageListTeacher(@ApiParam(name = "current",value = "请求第几页数据",required = true)
                           @PathVariable("current") Integer current,
                           @ApiParam(name = "limit",value = "每页有几条数据",required = true)
                           @PathVariable("limit") Integer limit) {
    Page<EduTeacher> page = new Page<>(current,limit);
    eduTeacherService.page(page,null);
    HashMap<String,Object> dataMap = new HashMap<>();
    dataMap.put("total",page.getTotal());
    dataMap.put("rows",page.getRecords());
    return R.ok().data(dataMap);
  }

  //  这个地方required=false表示 TeacherQuery可以 没有数据进行set注入
  @ApiOperation(value = "分页查询带条件")
  @PostMapping("/pageTeacherByWhere/{current}/{limit}")
  public R pageListTeacherByWhere(@ApiParam(name = "current",value = "请求第几页数据",required = true)
                                  @PathVariable("current") Integer current,
                                  @ApiParam(name = "limit",value = "每页有几条数据",required = true)
                                  @PathVariable("limit") Integer limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {
    //  page对象
    Page<EduTeacher> pageTeacher = new Page<>(current,limit);

    //  构建条件
    QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

    //  把前端传过来的条件分装到了对象当中，然后从对象中取出
    String name = teacherQuery.getName();
    Integer level = teacherQuery.getLevel();
    String begin = teacherQuery.getBegin();
    String end = teacherQuery.getEnd();

    //  判断条件值是否为空，如果不为空拼接条件
    if(!StringUtils.isEmpty(name)){
      wrapper.like("name",name);
    }
    if(!StringUtils.isEmpty(level)){
      wrapper.eq("level",level);
    }
    if(!StringUtils.isEmpty(begin)) {
      wrapper.ge("gmt_create",begin);
    }
    if(!StringUtils.isEmpty(end)) {
      wrapper.le("gmt_create",end);
    }

    //  排序 前端遍历渲染的时候效果更好
    wrapper.orderByDesc("gmt_create");

    //  第一个参数为page对象 第二个参数为条件对象
    eduTeacherService.page(pageTeacher,wrapper);

    HashMap<String,Object> dataMap = new HashMap<>();
    dataMap.put("total",pageTeacher.getTotal());
    dataMap.put("rows",pageTeacher.getRecords());

    return R.ok().data(dataMap);
  }

  @ApiOperation(value = "添加讲师")
  @PostMapping("/addTeacher")
  public R addTeacher(@RequestBody EduTeacher eduTeacher){
    boolean res = eduTeacherService.save(eduTeacher);
    if(res){
      return R.ok();
    }else{
      return R.error();
    }
  }

  @ApiOperation(value = "通过ID查询讲师")
  @GetMapping("/getTeacher/{id}")
  public R updateTeacher(@ApiParam(name = "id",value = "讲师id",required = true)
                         @PathVariable("id") String id) {
    EduTeacher eduTeacher = eduTeacherService.getById(id);
    if(eduTeacher != null){
      HashMap<String,Object> dataMap = new HashMap<>();
      dataMap.put("item",eduTeacher);
      return R.ok().data(dataMap);
    }else{
      return R.error();
    }
  }

  @ApiOperation(value = "修改教师")
  @PutMapping("/updateTeacher")
  public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
    boolean res = eduTeacherService.updateById(eduTeacher);
    if(res){
      return R.ok();
    }else{
      return R.error();
    }
  }

}

