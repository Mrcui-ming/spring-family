package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitVodClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {


  //  上传视频
  @Override
  public String uploadVideoAly(MultipartFile file) {

    String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;

    String accessKeySecret = ConstantVodUtils.ACCESS_KEY_SECRET;

    String fileName = file.getOriginalFilename();

    String title = fileName.substring(0, fileName.lastIndexOf("."));

    try{
      InputStream inputStream = file.getInputStream();

      //  第一步
      UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);

      //  第二步
      UploadVideoImpl uploader = new UploadVideoImpl();
      UploadStreamResponse response = uploader.uploadStream(request);

      //  第三步
      String videoId = "";
      if (response.isSuccess()) {
        videoId = response.getVideoId();
      } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
        videoId = response.getVideoId();
      }
      return videoId;

    }catch(Exception e){
      e.printStackTrace();
      return null;
    }

  }

  //  删除视频
  @Override
  public void removeVideoAly(String id) {
    String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;
    String accessKeySecret = ConstantVodUtils.ACCESS_KEY_SECRET;

    try {

      //  第一步 创建初始化client
      DefaultAcsClient client = InitVodClient.initVodClient(accessKeyId,accessKeySecret);

      //  第二步 创建删除对象
      DeleteVideoRequest request = new DeleteVideoRequest();
      request.setVideoIds(id);

      //  第三步 删除
      client.getAcsResponse(request);

    }catch (Exception e){
      e.printStackTrace();
      throw new GuliException(20001,"删除视频失败!");
    }
  }

  //  删除多条视频
  @Override
  public void removeMoreAlyVideo(List<String> videoIdList) {
    String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;
    String accessKeySecret = ConstantVodUtils.ACCESS_KEY_SECRET;

    try {
      //初始化对象
      DefaultAcsClient client = InitVodClient.initVodClient(accessKeyId, accessKeySecret);
      //创建删除视频request对象
      DeleteVideoRequest request = new DeleteVideoRequest();

      //videoIdList值转换成 1,2,3
      String videoIds = StringUtils.join(videoIdList.toArray(), ",");

      //向request设置视频id
      request.setVideoIds(videoIds);
      //调用初始化对象的方法实现删除
      client.getAcsResponse(request);
    }catch(Exception e) {
      e.printStackTrace();
      throw new GuliException(20001,"删除视频失败");
    }

  }

}

