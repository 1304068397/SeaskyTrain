package com.king.train.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(18)
@HeadRowHeight(20)
@ColumnWidth(18)
@Builder
public class ExportOut {

    @ExcelProperty(value = "学期",index = 0)
    private String xq = "20201";

    @ExcelProperty(value ="课程号",index = 1)
    private String  kch;

    @ExcelProperty(value ="班号",index = 2)
    private String  bh;

    @ExcelProperty(value ="学号",index = 3)
    private String  xh;

    @ExcelProperty(value = "学生姓名",index = 4)
    private String xsxm = "20201";

    @ExcelProperty(value ="平时成绩",index = 5)
    private String  pscj;

    @ExcelProperty(value ="期末成绩",index = 6)
    private String  qmcj;

    @ExcelProperty(value ="总评成绩",index = 7)
    private String  zpcj;

    @ExcelProperty(value ="成绩制式",index = 8)
    private String  cjzs;

    @ExcelProperty(value ="平时成绩比例",index = 9)
    private String  pscjbl;

    @ExcelProperty(value ="期末成绩比例",index = 10)
    private String  qmcjbl;

    @ExcelProperty(value ="所属图片",index = 11)
    private String  sstp;
}
