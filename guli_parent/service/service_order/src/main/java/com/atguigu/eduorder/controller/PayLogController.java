package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.PayLog;
import com.atguigu.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-02-03
 */
@RestController
@RequestMapping("/eduorder/paylog")
@CrossOrigin
public class PayLogController {

  @Autowired
  private PayLogService payLogService;

  //  生成微信二维码
  @GetMapping("/createNative/{orderNo}")
  public R createNative(@PathVariable("orderNo") String orderNo){
    Map dataMap = payLogService.createNatvie(orderNo);
    return R.ok().data(dataMap);
  }

  //  查看订单状态
  @GetMapping("/queryPayStatus/{orderNo}")
  public R queryPayStatus(@PathVariable("orderNo") String orderNo){
    //  请求微信提供的固定地址 查看订单状态 返回Map集合
    Map<String,String> map = payLogService.queryPayStatus(orderNo);
    if(map == null) {
      return R.error().message("支付出错了");
    }
    //如果返回map里面不为空，通过map获取订单状态
    //支付成功
    if(map.get("trade_state").equals("SUCCESS")) {
      //添加记录到支付表，更新订单表订单状态
      payLogService.updateOrdersStatus(map);
      return R.ok().message("支付成功");
    }
    return R.ok().code(25000).message("支付中");
  }

}

