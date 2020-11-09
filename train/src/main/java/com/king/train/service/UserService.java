package com.king.train.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.king.train.config.validation.ValidationGroup;
import com.king.train.entities.CommonResult;
import com.king.train.entities.TbUser;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface UserService extends IService<TbUser> {

    @Validated({ValidationGroup.Update.class})
    CommonResult saveOrUpdateUser(@Valid TbUser param);

    @Validated({ValidationGroup.Login.class})
    CommonResult login(@Valid TbUser param, DefaultWebSecurityManager defaultSecurityManager);

    CommonResult index(String mobile);

    CommonResult logout();

    CommonResult getCurrentUser();

    CommonResult selectUserPage(TbUser param);
}
