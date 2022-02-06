package com.atguigu.vod.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
  String uploadVideoAly(MultipartFile file);

  void removeVideoAly(String id) throws ClientException;

  void removeMoreAlyVideo(List<String> videoIdList);
}
