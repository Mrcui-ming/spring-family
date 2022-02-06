package com.atguigu.commonutils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义公共的接口返回值
 * {
 *   success: boolean,
 *   code: int,
 *   message: String,
 *   data: {
 *     total: int,
 *     rows: [{},{},{},{}]
 *   }
 * }
 * 使用以上这个数据结构进行创建类
 * */
@Data
public class R {

  @ApiModelProperty(value = "是否成功")
  private Boolean success;

  @ApiModelProperty(value = "状态码")
  private Integer code;

  @ApiModelProperty(value = "返回消息")
  private String message;

  @ApiModelProperty(value = "返回数据")
  private Map<String, Object> data = new HashMap<String, Object>();

  /**
   * 私有构造的目的是为了规范对象的创建 只能通过R.ok() R.error()创建
   * */
  private R(){};

  // 成功的静态方法
  public static R ok() {
    R r = new R();
    r.setSuccess(true);
    r.setCode(ResultCode.SUCCESS);
    r.setMessage("成功");
    return r;
  }

  // 失败的静态方法
  public static R error() {
    R r = new R();
    r.setSuccess(false);
    r.setCode(ResultCode.ERROR);
    r.setMessage("失败");
    return r;
  }

  /**
   * 给每个属性提供一个实例方法是为了链式编程
   * 例:
   * R.ok().success(true).code(20000).message("成功").data(map)
   * */
  public R success(Boolean success) {
    this.setSuccess(success);
    return this;
  }

  public R code(Integer code) {
    this.setCode(code);
    return this;
  }

  public R message(String message) {
    this.setMessage(message);
    return this;
  }

  public R data(String key, Object value){
    this.data.put(key, value);
    return this;
  }

  public R data(Map<String, Object> map){
    this.setData(map);
    return this;
  }

}
