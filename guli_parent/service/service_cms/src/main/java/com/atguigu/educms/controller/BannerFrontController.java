package com.atguigu.educms.controller;

import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  前端Banner控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/educms/bannerfront")
public class BannerFrontController {

  @Autowired
  private CrmBannerService bannerService;

  @GetMapping("/getAllBanner")
  public R getAllBanner() {
    List<CrmBanner> banners = bannerService.getAllBanner();
    return R.ok().data("items",banners);
  }

}
