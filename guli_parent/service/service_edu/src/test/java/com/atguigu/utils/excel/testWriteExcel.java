package com.atguigu.utils.excel;

import com.alibaba.excel.EasyExcel;
import java.util.ArrayList;
import java.util.List;

public class testWriteExcel {

  public static void main(String[] args) {

    //  实现excel写的操作
    //  1 设置写入文件夹地址和excel文件名称
    //  String filename = "C:\\Users\\cmm\\Documents\\write.xlsx";
    //  2 调用easyexcel里面的方法实现写操作
    //  write方法两个参数：第一个参数文件路径名称，第二个参数实体类class
    //  EasyExcel.write(filename,TestDemo.class).sheet("学生列表").doWrite(getList());

    //  实现excel读操作
    String filename = "C:\\Users\\cmm\\Documents\\write.xlsx";
    EasyExcel.read(filename,TestDemo.class,new ExcelListener()).sheet().doRead();
  }

  public static List<TestDemo> getList(){
    List<TestDemo> lists = new ArrayList<>();
    for(int i = 0; i < 10; i++){
      TestDemo testDemo = new TestDemo();
      testDemo.setSno(new Integer(i).toString());
      testDemo.setSname("阿栈" + i);
      lists.add(testDemo);
    }
    return lists;
  }
}
