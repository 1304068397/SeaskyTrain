package com.king.train.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.king.train.entities.CommonResult;
import com.king.train.entities.TbUser;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

public interface UserService extends IService<TbUser> {

    @Validated
    CommonResult saveOrUpdateUser(@Valid TbUser param);
}
