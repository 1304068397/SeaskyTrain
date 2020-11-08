package com.king.train.ctrl;

import com.king.train.entities.CommonResult;
import com.king.train.entities.TbUser;
import com.king.train.enums.ResponseCode;
import com.king.train.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @PackageName:com.king.train.ctrl
 * @ClassName:UserCtrl001
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/8 21:24
 */
@Api(tags = "用户Ctrl")
@RestController
@RequestMapping("UserCtrl001")
public class UserCtrl001 {

    @Resource
    UserService userService;

    @ApiOperation("新增或编辑")
    @PostMapping("saveOrUpdateUser")
    public CommonResult saveOrUpdateUser(@RequestBody TbUser param){
        return userService.saveOrUpdateUser(param);
    }

    @ApiOperation("根据id查询")
    @PostMapping("queryById")
    public CommonResult queryById(@RequestBody TbUser param){
        TbUser tbUser = userService.getById(param.getId());
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message(),tbUser);
    }
}
