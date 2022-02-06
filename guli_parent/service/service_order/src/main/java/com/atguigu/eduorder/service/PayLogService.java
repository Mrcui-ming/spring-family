package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-02-03
 */
public interface PayLogService extends IService<PayLog> {

  Map createNatvie(String orderNo);

  Map<String, String> queryPayStatus(String orderNo);

  @Transactional
  void updateOrdersStatus(Map<String, String> map);
}
