package com.atguigu.educms.controller;

import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  后端Banner控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/educms/banneradmin")
public class BannerAdminController {

  @Autowired
  private CrmBannerService bannerService;

  @GetMapping("/getBannerList/{current}/{limit}")
  public R getBannerList(@PathVariable("current") Integer current,
                         @PathVariable("limit") Integer limit) {
    Page<CrmBanner> page = new Page<>(current,limit);
    bannerService.page(page,null);
    return R.ok().data("items",page.getRecords()).data("total",page.getTotal());
  }

  @GetMapping("/getBannerById/{id}")
  public R getBannerById(@PathVariable("id") String id) {
    CrmBanner banner = bannerService.getById(id);
    return R.ok().data("item",banner);
  }

  @DeleteMapping("/{id}")
  public R deleteBanner(@PathVariable("id") String id) {
    bannerService.removeById(id);
    return R.ok();
  }

  @PostMapping("/addBanner")
  public R addBanner(@RequestBody CrmBanner banner) {
    bannerService.save(banner);
    return R.ok();
  }

  @PostMapping("/updateBanner")
  public R updateBanner(@RequestBody CrmBanner banner){
    bannerService.updateById(banner);
    return R.ok();
  }

}

