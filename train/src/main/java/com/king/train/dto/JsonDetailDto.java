package com.king.train.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @PackageName:com.example.demo.dto
 * @ClassName:JsonDetailDto
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2021/3/23 12:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JsonDetailDto {
    private List<List<String>> Table1;
    private String 上课教室;
    private String 学分;
    private String 成绩制式;
    private String 成绩比例;
    private String 教师;
    private String 校区;
    private String 班号;
    private String 课程号;
    private String 课程名称;
}
