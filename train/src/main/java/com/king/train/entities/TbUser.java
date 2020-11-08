package com.king.train.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @PackageName:com.king.train.entities
 * @ClassName:tbUser
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/8 19:18
 */
@ApiModel("用户表")
@TableName(value = "tbUser", autoResultMap = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TbUser {

    @ApiModelProperty(value = "id", example = "1")
    @NotNull(message = "姓名不能为空！")
    private Long id;
    @ApiModelProperty(value = "姓名", example = "王宗保")
    @NotEmpty(message = "姓名不能为空！")
    private String userName;
    @ApiModelProperty(value = "密码", example = "123456")
    @NotEmpty(message = "密码不能为空！")
    private String password;
}
