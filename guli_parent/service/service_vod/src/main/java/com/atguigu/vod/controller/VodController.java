package com.atguigu.vod.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.User;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitVodClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping("/eduvod/video")
public class VodController {

  @Autowired
  private VodService vodService;

  //  上传视频
  @PostMapping("/uploadAlyiVideo")
  public R uploadAlyiVideo(MultipartFile file) {
    String videoId = vodService.uploadVideoAly(file);
    return R.ok().data("videoId",videoId);
  }

  //  删除视频
  @DeleteMapping("/removeAlyVideo/{id}")
  public R removeAlyVideo(@PathVariable("id") String id) throws ClientException {
    vodService.removeVideoAly(id);
    return R.ok();
  }

  //  批量删除视频
  @DeleteMapping("/removeMoreAlyVideo")
  public R removeMoreAlyVideo(@RequestParam("videoIdList") List<String> videoIdList) {
    vodService.removeMoreAlyVideo(videoIdList);
    return R.ok();
  }

  //根据视频id获取视频凭证
  @GetMapping("getPlayAuth/{id}")
  public R getPlayAuth(@PathVariable("id") String id) {
    try {
      String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;

      String accessKeySecret = ConstantVodUtils.ACCESS_KEY_SECRET;

      //  根据视频id获取视频播放地址
      //  第1步: 创建初始化对象
      DefaultAcsClient client = InitVodClient.initVodClient(accessKeyId, accessKeySecret);

      GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
      GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

      //  第2步: 向request对象中传入视频id
      request.setVideoId(id);

      //  第3步: 调用初始化对象里面的方法
      response = client.getAcsResponse(request);

      //  第4步: 获取视频凭证
      String pz = response.getPlayAuth();
      System.out.println(pz);
      return R.ok().data("pz",pz);

    }catch (Exception e){
      throw new GuliException(20001,"获取凭证失败");
    }

  }

}
