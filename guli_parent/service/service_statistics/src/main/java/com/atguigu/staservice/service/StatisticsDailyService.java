package com.atguigu.staservice.service;

import com.atguigu.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2021-02-05
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

  @Transactional
  void registerCount(String day);

  Map<String, Object> getChartsInfo(String type, String begin, String end);
}
