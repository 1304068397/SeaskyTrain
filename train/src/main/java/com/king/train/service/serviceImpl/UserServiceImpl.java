package com.king.train.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.king.train.config.JavaxGroup.Update;
import com.king.train.dao.UserMapper;
import com.king.train.entities.CommonResult;
import com.king.train.entities.TbUser;
import com.king.train.enums.ResponseCode;
import com.king.train.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    //redis
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    @Validated({Update.class})
    public CommonResult saveOrUpdateUser(TbUser param) {
        saveOrUpdate(param);
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message(),param);
    }

    @Override
    public CommonResult login(TbUser param,    DefaultWebSecurityManager defaultSecurityManager) {
        //登入前校验验证码
        String verificationCode = redisTemplate.boundValueOps(param.getUserName()).get();
        if (StringUtils.isEmpty(verificationCode)){
            return new CommonResult<>(ResponseCode.FAILURE.value(),"验证码已失效，请重新获取！");
        }
        if (!verificationCode.equals(param.getVerificationCode())){
            return new CommonResult<>(ResponseCode.FAILURE.value(),"验证码不匹配");
        }
        //将securityManager设置当前的运行环境中
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //从SecurityUtils里边创建一个subject
        Subject subject = SecurityUtils.getSubject();
        //在认证提交前准备token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(param.getUserName(), param.getPassword());

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new CommonResult<>(ResponseCode.FAILURE.value(),ResponseCode.FAILURE.message());
        }

        TbUser user = (TbUser) subject.getPrincipal();
        subject.getSession().setAttribute("userLogin", user);

        return new CommonResult<>(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message());
    }

    @Override
    public CommonResult index(String mobile) {
        String verificationCode = UUID.randomUUID().toString().substring(0,4);
        redisTemplate.boundValueOps(mobile).set(verificationCode);
        redisTemplate.expire(mobile, 60, TimeUnit.SECONDS);
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message(),verificationCode);
    }
}
