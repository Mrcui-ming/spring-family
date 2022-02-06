package com.atguigu.staservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-02-05
 */
@RestController
@RequestMapping("/staservice/sta")
@CrossOrigin
public class StatisticsDailyController {

  @Autowired
  private StatisticsDailyService dailyService;

  //统计某一天注册人数,生成统计数据
  @PostMapping("/registerCount/{day}")
  public R registerCount(@PathVariable("day") String day) {
    dailyService.registerCount(day);
    return R.ok();
  }

  @GetMapping("/getChartsInfo/{type}/{begin}/{end}")
  public R getChartsInfo(@PathVariable("type") String type,
                         @PathVariable("begin") String begin,
                         @PathVariable("end") String end) {
    Map<String,Object> dataMap = dailyService.getChartsInfo(type,begin,end);
    return R.ok().data(dataMap);
  }

}

