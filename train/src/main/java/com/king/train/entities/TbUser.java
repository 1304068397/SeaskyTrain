package com.king.train.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.king.train.config.validation.ValidationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

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
public class TbUser implements Serializable {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;
    @ApiModelProperty(value = "姓名", example = "王宗保")
    @NotEmpty(groups = {ValidationGroup.Login.class,ValidationGroup.Update.class},message = "姓名不能为空！")
    private String userName;
    @ApiModelProperty(value = "密码", example = "123456")
    @NotEmpty(groups = {ValidationGroup.Login.class,ValidationGroup.Update.class},message = "密码不能为空！")
    @Size(groups = {ValidationGroup.Login.class,ValidationGroup.Update.class},min=6,max = 20)
    private String password;
    @ApiModelProperty(value = "验证码", example = "0cd1")
    @NotEmpty(groups = {ValidationGroup.Login.class},message = "验证码不能为空！")
    @TableField(exist = false)
    private String verificationCode;
    @ApiModelProperty(value = "pageIndex", example = "1")
    @TableField(exist = false)
    private Integer pageIndex;
    @ApiModelProperty(value = "pageSize", example = "10")
    @TableField(exist = false)
    private Integer pageSize;
}
