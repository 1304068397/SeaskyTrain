package com.king.train.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PackageName:com.example.demo.utils
 * @ClassName:Dto
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2021/3/22 17:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dto {
    private String image;
    private template_list configure;
}
