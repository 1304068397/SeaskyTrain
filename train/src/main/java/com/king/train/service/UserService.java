package com.king.train.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.king.train.config.JavaxGroup.Update;
import com.king.train.entities.CommonResult;
import com.king.train.entities.TbUser;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Validated
public interface UserService extends IService<TbUser> {

    @Validated({Update.class})
    CommonResult saveOrUpdateUser(@Valid TbUser param);

    CommonResult login(TbUser param, DefaultWebSecurityManager defaultSecurityManager);

    CommonResult index(String mobile);

    CommonResult logout();

    CommonResult getCurrentUser();

    CommonResult selectUserPage(TbUser param);
}
