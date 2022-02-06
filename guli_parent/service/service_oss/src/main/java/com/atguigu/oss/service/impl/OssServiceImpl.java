package com.atguigu.oss.service.impl;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class OssServiceImpl implements OssService {

  @Override
  public String uploadFileAvatar(MultipartFile file) {

    // 获取地址/id/密钥/bucket仓库名称
    String endpoint = ConstantPropertiesUtils.END_POIND;
    String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
    String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
    String bucketname = ConstantPropertiesUtils.BUCKET_NAME;

    try{

      //  创建OSSClient实例。
      OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

      //  获取文件名称 eb3458fcc4a84f49a5d05231a4bd3f4chd.jpg
      String fileName = file.getOriginalFilename();
      fileName = IdUtil.simpleUUID() + fileName;

      //  获取层级文件夹路径 2021/1/20 -> 2021/1/20/eb3458fcc4a84f49a5d05231a4bd3f4chd.jpg
      String datePath = new DateTime().toString("yyyy/MM/dd");
      fileName = datePath + "/" + fileName;

      //  获取文件流
      InputStream inputStream = file.getInputStream();

      // 创建PutObjectRequest对象。fileName可以为 /a/b/c.jpg 也可以是 c.jpg
      PutObjectRequest putObjectRequest = new PutObjectRequest(bucketname, fileName , inputStream);

      // 上传文件。
      ossClient.putObject(putObjectRequest);

      // 关闭OSSClient。
      ossClient.shutdown();

      //https://guli-system.oss-cn-beijing.aliyuncs.com/2021/01/20/eb3458fcc4a84f49a5d05231a4bd3f4chd.jpg
      StringBuilder url = new StringBuilder();
      url.append("https://").append(bucketname).append(".").append(endpoint).append("/").append(fileName);

      return url.toString();

    }catch(Exception e){
      e.printStackTrace();
      return null;
    }
  }

}
