package com.atguigu.eduorder.controller;

import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class OrderController {

  @Autowired
  private OrderService orderService;

  // 生成订单的方法 返回订单号用以支付的时候查询订单
  @GetMapping("/createOrder/{courseId}")
  public R saveOrder(@PathVariable("courseId") String courseId,
                     HttpServletRequest request) {
    String orderNo = orderService.createOrders(courseId,request.getHeader("token"));
    return R.ok().data("item",orderNo);
  }

  //  根据订单号 获取订单
  @GetMapping("/getOrderInfo/{orderId}")
  public R getOrderInfo(@PathVariable("orderId") String orderId) {
    QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
    orderWrapper.eq("order_no",orderId);
    Order order = orderService.getOne(orderWrapper);
    return R.ok().data("item",order);
  }

  //  根据用户id和课程id查询 课程是否已经支付
  @GetMapping("/getOrderInfoByQuery/{courseId}/{memberId}")
  public boolean getOrderInfoByQuery(@PathVariable("courseId") String courseId,
                                     @PathVariable("memberId") String memberId){
    QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
    orderWrapper.eq("course_id",courseId);
    orderWrapper.eq("member_id",memberId);
    orderWrapper.eq("status",1);
    int count = orderService.count(orderWrapper);
    return count > 0;
  }

}

