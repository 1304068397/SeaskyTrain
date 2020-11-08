package com.king.train.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.king.train.dao.UserMapper;
import com.king.train.entities.CommonResult;
import com.king.train.entities.TbUser;
import com.king.train.enums.ResponseCode;
import com.king.train.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * @PackageName:com.king.train.service.serviceImpl
 * @ClassName:UserServiceImpl
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/8 21:31
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, TbUser> implements UserService {

    @Override
    public CommonResult saveOrUpdateUser(@Valid TbUser param) {
        saveOrUpdate(param);
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message(),param);
    }
}
