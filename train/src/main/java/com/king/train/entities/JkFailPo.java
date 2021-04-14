package com.king.train.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PackageName:com.example.demo.po
 * @ClassName:JkSuccessPo
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2021/3/25 9:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "jk_fail",autoResultMap = true)
public class JkFailPo {

    private Long id;
    private String name;
    private String detail;
}
