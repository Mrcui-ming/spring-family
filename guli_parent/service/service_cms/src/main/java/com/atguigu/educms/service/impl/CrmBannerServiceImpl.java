package com.atguigu.educms.service.impl;

import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.mapper.CrmBannerMapper;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-01-28
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

  @Override
  @Cacheable(value = "banner",key = "'selectIndexList'") //主要做查询操作
  public List<CrmBanner> getAllBanner() {

    QueryWrapper<CrmBanner> bannerWrapper = new QueryWrapper<>();
    bannerWrapper.orderByDesc("id");
    bannerWrapper.last("limit 0,2");

    List<CrmBanner> lists = baseMapper.selectList(bannerWrapper);
    return lists;

  }
}
