package com.atguigu.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import java.util.Map;

public class ExcelListener extends AnalysisEventListener<TestDemo> {

  //一行一行读取excel内容 data就是每行内容
  @Override
  public void invoke(TestDemo data, AnalysisContext analysisContext) {
    System.out.println("学生数据"+data);
  }

  //读取表头内容
  @Override
  public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
    System.out.println("表头："+headMap);
  }

  //读取完成之后
  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    System.out.println("读取excel表格完成");
  }
}