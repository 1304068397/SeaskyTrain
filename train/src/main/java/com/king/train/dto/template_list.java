package com.king.train.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @PackageName:com.example.demo.utils
 * @ClassName:template_list
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2021/3/22 17:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class template_list {
    private List<String> template_list;
}
