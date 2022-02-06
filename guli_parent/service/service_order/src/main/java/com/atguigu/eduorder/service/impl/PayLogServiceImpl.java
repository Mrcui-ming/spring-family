package com.atguigu.eduorder.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.PayLog;
import com.atguigu.eduorder.mapper.PayLogMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.service.PayLogService;
import com.atguigu.eduorder.utils.ConstantPropertiesUtils;
import com.atguigu.eduorder.utils.HttpClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-02-03
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

  @Autowired
  private OrderService orderService;

  @Override
  public Map createNatvie(String orderNo) {
    try {
      //  根据订单号查询订单信息
      QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
      orderWrapper.eq("order_no",orderNo);
      Order order = orderService.getOne(orderWrapper);

      //  使用map设置生成二维码需要参数
      Map m = new HashMap();
      m.put("appid", ConstantPropertiesUtils.APP_ID); //appid
      m.put("mch_id", ConstantPropertiesUtils.PARTNER); // 商户号
      m.put("nonce_str", WXPayUtil.generateNonceStr()); // 唯一的二维码key
      m.put("body", order.getCourseTitle()); //课程标题
      m.put("out_trade_no", orderNo); //订单号
      //  价格(要求字符串)
      m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
      //支付的ip地址 如果是https://www.baidu.com的话就写baidu.com 现在处于本地
      m.put("spbill_create_ip", "127.0.0.1");
      m.put("notify_url", ConstantPropertiesUtils.NOTIFY_URL); //回调函数的地址(本业务中不需要)
      m.put("trade_type", "NATIVE"); //类型 这个类型表示根据价格生成二维码

      //  发送httpclient请求，传递参数xml格式，微信支付提供的固定的地址
      HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
      //设置xml格式的参数(要求字符串) 这个商户key为了加密使用
      client.setXmlParam(WXPayUtil.generateSignedXml(m,ConstantPropertiesUtils.PARTNER_KEY));
      client.setHttps(true);
      //执行post请求发送
      client.post();

      //  得到发送请求返回结果(把字符串转为Map集合)
      String xml = client.getContent();
      Map<String,String> resultMap = WXPayUtil.xmlToMap(xml);

      //最终返回数据 的封装
      Map map = new HashMap();
      map.put("out_trade_no", orderNo);                      //订单号
      map.put("course_id", order.getCourseId());             //课程id
      map.put("total_fee", order.getTotalFee());             //课程价格
      map.put("result_code", resultMap.get("result_code"));  //返回二维码操作状态码
      map.put("code_url", resultMap.get("code_url"));        //二维码地址

      return map;

    }catch (Exception e){
      e.printStackTrace();
      throw new GuliException(20001,"生成二维码失败");
    }
  }

  @Override
  public Map<String, String> queryPayStatus(String orderNo) {
    try {
      //  封装参数
      Map m = new HashMap<>();
      m.put("appid", ConstantPropertiesUtils.APP_ID); //  appid
      m.put("mch_id", ConstantPropertiesUtils.PARTNER); //  商户key
      m.put("out_trade_no", orderNo); //  订单号
      m.put("nonce_str", WXPayUtil.generateNonceStr()); // 唯一的二维码key

      //  发送httpclient
      HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
      client.setXmlParam(WXPayUtil.generateSignedXml(m,ConstantPropertiesUtils.PARTNER_KEY));
      client.setHttps(true);
      client.post();

      //  得到请求返回内容
      String xml = client.getContent();
      Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
      return resultMap;

    }catch(Exception e) {
      return null;
    }
  }

  @Override
  public void updateOrdersStatus(Map<String, String> map) {
    String orderNo = map.get("out_trade_no");
    QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
    orderWrapper.eq("order_no",orderNo);
    Order order = orderService.getOne(orderWrapper);

    //  如果订单表中已经是支付成功状态 那么就没必要去再去更改订单表
    if(order.getStatus().intValue() == 1) return;
    order.setStatus(1);
    orderService.updateById(order);

    //  创建支付表的对象 并添加到数据库
    PayLog payLog = new PayLog();
    payLog.setOrderNo(orderNo);
    payLog.setPayTime(new Date());
    payLog.setPayType(1);
    payLog.setTotalFee(order.getTotalFee());

    payLog.setAttr(JSON.toJSONString(map)); //其他信息 干脆把全部信息转成json存起来
    payLog.setTradeState(map.get("trade_state")); //支付状态
    payLog.setTransactionId(map.get("transaction_id")); //流水号

    baseMapper.insert(payLog);
  }

}
