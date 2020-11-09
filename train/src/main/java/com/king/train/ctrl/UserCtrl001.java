package com.king.train.ctrl;

import com.king.train.config.druid.CurDataSource;
import com.king.train.config.druid.DataSourceName;
import com.king.train.entities.CommonResult;
import com.king.train.entities.TbUser;
import com.king.train.enums.ResponseCode;
import com.king.train.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    // shiro
    @Resource
    DefaultWebSecurityManager defaultSecurityManager;

    @Resource
    UserService userService;

    @ApiOperation("新增或编辑")
    @PostMapping("saveOrUpdateUser")
    public CommonResult saveOrUpdateUser(@RequestBody TbUser param){
        return userService.saveOrUpdateUser(param);
    }

    @ApiOperation("根据id查询")
    @PostMapping("queryById")
    @CurDataSource(source = DataSourceName.read)
    public CommonResult queryById(@RequestBody TbUser param){
        TbUser tbUser = userService.getById(param.getId());
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message(),tbUser);
    }

    @ApiOperation("登入")
    @PostMapping("/login")
    public CommonResult login(@RequestBody TbUser param){
        return userService.login(param,defaultSecurityManager);
    }

    @ApiOperation("获取验证码（60s）")
    @GetMapping("/index")
    public CommonResult index(@RequestParam(name = "userName") String userName){
        return userService.index(userName);
    }

    @ApiOperation("注销")
    @PostMapping("/logout")
    public CommonResult logout(){
        return userService.logout();
    }

    @ApiOperation("获取当前登陆人信息")
    @PostMapping("/getCurrentUser")
    public CommonResult getCurrentUser(){
        return userService.getCurrentUser();
    }

    @ApiOperation("查询列表Page")
    @PostMapping("/selectUserPage")
    @CurDataSource(source = DataSourceName.read)
    public CommonResult selectUserPage(@RequestBody TbUser param){
        return userService.selectUserPage(param);
    }
}
