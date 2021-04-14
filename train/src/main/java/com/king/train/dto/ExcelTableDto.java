package com.king.train.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelTableDto {

    private String 学号;
    private String 姓名;
    private String 平时成绩;
    private String 期末成绩;
    private String 总评成绩;
    private String 学院;
    private String 专业;
    private String 备注;

}
