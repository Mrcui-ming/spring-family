package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.eduservice.client.VodFeignService;
import com.netflix.client.ClientException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-01-22
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {

  @Autowired
  private EduVideoService videoService;

  @Autowired
  private VodFeignService vodFeignService;

  //  添加小节
  @PostMapping("/addVideo")
  public R addVideo(@RequestBody EduVideo video) {
    videoService.save(video);
    return R.ok();
  }

  //  根据id查询小节
  @GetMapping("/getVideoInfo/{videoId}")
  public R getVideoInfo(@PathVariable("videoId") String videoId) {
    EduVideo video = videoService.getById(videoId);
    return R.ok().data("item",video);
  }

  //  更新小节
  @PostMapping("/updateVideo")
  public R updateVideo(@RequestBody EduVideo video) {
    videoService.updateById(video);
    return R.ok();
  }

  //  删除小节 1删除惧库中的信息 2删除阿里云点播里面的信息
  //  注意细节 如果小节中没有存储视频 那么就不应该调用删除视频的方法
  @DeleteMapping("/{videoId}")
  @GlobalTransactional(name = "fsp-delete-video",rollbackFor = Exception.class)
  public R deleteVideo(@PathVariable("videoId") String videoId) throws ClientException {

    EduVideo video = videoService.getById(videoId);
    String videoSourceId = video.getVideoSourceId();

    if(!StringUtils.isEmpty(videoSourceId)){
      vodFeignService.removeAlyVideo(videoSourceId);
    }

    videoService.removeById(videoId);

    return R.ok();
  }

}

