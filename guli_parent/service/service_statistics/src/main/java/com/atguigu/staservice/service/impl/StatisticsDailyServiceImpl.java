package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcentFeignService;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-02-05
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

  @Autowired
  private UcentFeignService ucentFeignService;

  @Autowired
  private StatisticsDailyService dailyService;

  @Override
  public void registerCount(String day) {

    //  先将重复天数的记录删掉再去插入记录 因为不想有相同日期的记录表
    QueryWrapper<StatisticsDaily> staWrapper = new QueryWrapper<>();
    staWrapper.eq("date_calculated",day);
    baseMapper.delete(staWrapper);

    //  查询到当天注册的人数
    R registerCount = ucentFeignService.getMemberCount(day);
    int countRegister = (Integer) registerCount.getData().get("registerCount");

    //  将人数添加到数据统计表中
    StatisticsDaily sta = new StatisticsDaily();
    sta.setRegisterNum(countRegister); //注册人数
    sta.setDateCalculated(day);//统计日期

    sta.setVideoViewNum(RandomUtils.nextInt(100,200));
    sta.setLoginNum(RandomUtils.nextInt(100,200));
    sta.setCourseNum(RandomUtils.nextInt(100,200));

    baseMapper.insert(sta);
  }

  @Override
  public Map<String, Object> getChartsInfo(String type, String begin, String end) {

    QueryWrapper<StatisticsDaily> staWrapper = new QueryWrapper<>();
    staWrapper.between("date_calculated",begin,end);
    staWrapper.select("date_calculated",type);

    List<StatisticsDaily> staList = baseMapper.selectList(staWrapper);

    List<String> dateList = new ArrayList<>();
    List<String> typeList = new ArrayList<>();
    for(int i  = 0; i < staList.size(); i++){
      StatisticsDaily sta = staList.get(i);
      dateList.add(sta.getDateCalculated());
      switch (type){
        case "register_num":
          typeList.add(sta.getRegisterNum().toString());
          break;
        case "login_num":
          typeList.add(sta.getLoginNum().toString());
          break;
        case "video_view_num":
          typeList.add(sta.getVideoViewNum().toString());
          break;
        case "course_num":
          typeList.add(sta.getCourseNum().toString());
          break;
      }
    }

    Map<String,Object> dataMap = new HashMap<>();
    dataMap.put("dateList",dateList);
    dataMap.put("typeList",typeList);
    return dataMap;
  }

}
