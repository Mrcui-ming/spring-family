package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.couseVo.CourseFeignVo;
import com.atguigu.eduorder.client.CourseFeignService;
import com.atguigu.eduorder.client.UcentFeignService;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-02-03
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

  @Autowired
  private CourseFeignService courseFeignService;

  @Autowired
  private UcentFeignService ucentFeignService;

  @Override
  public String createOrders(String courseId, String token) {

    //  根据课程id查询课程信息
    CourseFeignVo courseInfoOrder = courseFeignService.getCourseInfoOrder(courseId);

    //  根据请求头中的token查询用户信息
    R memberInfoByToken = ucentFeignService.getMemberInfoByToken(token);
    Map<String, Object> ucentInfo = memberInfoByToken.getData();
    String memberId = (String)((HashMap)ucentInfo.get("item")).get("id");
    String nickname = (String)((HashMap)ucentInfo.get("item")).get("nickname");
    String mobile = (String)((HashMap)ucentInfo.get("item")).get("mobile");

    //  创建订单对象插入数据库
    Order order = new Order();
    order.setOrderNo(OrderNoUtil.getOrderNo());

    order.setCourseCover(courseInfoOrder.getCover());
    order.setCourseId(courseInfoOrder.getId());
    order.setCourseTitle(courseInfoOrder.getTitle());
    order.setTeacherName(courseInfoOrder.getTeacherName());
    order.setTotalFee(courseInfoOrder.getPrice());

    order.setMemberId(memberId);
    if(!StringUtils.isEmpty(mobile)){
      order.setMobile(mobile);
    }
    order.setNickname(nickname);

    order.setStatus(0);  //订单状态（0：未支付 1：已支付）
    order.setPayType(1);  //支付类型 ，微信1
    baseMapper.insert(order);
    //  返回订单号

    return order.getOrderNo();
  }
}
