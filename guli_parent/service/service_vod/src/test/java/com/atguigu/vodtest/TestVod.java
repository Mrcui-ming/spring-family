package com.atguigu.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

public class TestVod {

  public static void main(String[] args) throws ClientException {

    //  获取视频凭证
    getVideoPz();

    //  获取视频播放地址 和 格式信息
    //getVideoPath();

    //  上传视频
    //uploadVideo();
  }

  /**
   * 阿里云上传视频
   * */
  public static void uploadVideo() {
    String accessKeyId = "LTAI4GEqHgi9WxYk3pCQ56pD";
    String accessKeySecret = "YWuLlE9O0L44DC2Sl7qqtFDxh6vYne";
    //  上传到阿里云之后的视频名称 一般和filename相同即可
    String title = "6 - What If I Want to Move Faster.mp4 - host upload";
    //  本地文件路径
    String fileName = "C:\\Users\\cmm\\Documents\\6 - What If I Want to Move Faster.mp4";

    //上传视频的方法
    UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
    /* 可指定分片上传时每个分片的大小，默认为2M字节 */
    request.setPartSize(2 * 1024 * 1024L);
    /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
    request.setTaskNum(1);

    UploadVideoImpl uploader = new UploadVideoImpl();
    UploadVideoResponse response = uploader.uploadVideo(request);

    if (response.isSuccess()) {
      System.out.print("VideoId=" + response.getVideoId() + "\n");
    } else {
      /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
      System.out.print("VideoId=" + response.getVideoId() + "\n");
      System.out.print("ErrorCode=" + response.getCode() + "\n");
      System.out.print("ErrorMessage=" + response.getMessage() + "\n");
    }
  }

  /**
   * 根据视频id获取视频凭证 通过视频凭证配个视频播放器实现播放视频
   */
  public static void getVideoPz() throws ClientException {
    //  根据视频id获取视频播放地址
    //  第1步: 创建初始化对象
    DefaultAcsClient client = InitObject.initVodClient("LTAI4GEqHgi9WxYk3pCQ56pD", "YWuLlE9O0L44DC2Sl7qqtFDxh6vYne");

    GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
    GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

    //  第2步: 向request对象中传入视频id
    request.setVideoId("86ea02918a304768bfe755ef58c4eaae");

    //  第3步: 调用初始化对象里面的方法
    response = client.getAcsResponse(request);

    //  第4步: 获取视频凭证
    String pz = response.getPlayAuth();
    System.out.println("PlayAuth = " + pz);
  }

  /**
   * 根据视频id获取视频地址 实现播放视频
   */
  public static void getVideoPath() throws ClientException {
    //  根据视频id获取视频播放地址
    //  第1步: 创建初始化对象
    DefaultAcsClient client = InitObject.initVodClient("LTAI4GEqHgi9WxYk3pCQ56pD", "YWuLlE9O0L44DC2Sl7qqtFDxh6vYne");

    GetPlayInfoRequest request = new GetPlayInfoRequest();
    GetPlayInfoResponse response = new GetPlayInfoResponse();

    //  第2步: 向request对象中传入视频id
    request.setVideoId("86ea02918a304768bfe755ef58c4eaae");

    //  第3步: 调用初始化对象里面的方法
    response = client.getAcsResponse(request);

    //  第4步: 获取播放地址 和 视频名称
    List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
    for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
      System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
    }

    //获取Base信息
    System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
  }

}
