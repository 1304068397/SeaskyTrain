package com.king.train.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

import java.util.List;
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
    public CommonResult saveOrUpdateUser(TbUser param) {
        //校验 ： 用户名唯一
        List<TbUser> list = list(new QueryWrapper<>(TbUser.builder().userName(param.getUserName()).build()));
        if (list.size()>1){
            return new CommonResult(ResponseCode.FAILURE.value(),"用户名不唯一！");
        }
        if (list.size()==1){
            boolean flag = list.get(0).getUserName().equals("当前登录人username");
            if (!flag){
                return new CommonResult(ResponseCode.FAILURE.value(),"用户名不唯一！");
            }
        }
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

    @Override
    public CommonResult logout() {
        //登出清除缓存
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message());
    }

    @Override
    public CommonResult getCurrentUser() {
        TbUser user = (TbUser) SecurityUtils.getSubject().getPrincipal();
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message(),user);
    }

    @Override
    public CommonResult selectUserPage(TbUser param) {
        Page<TbUser> page = new Page<>(param.getPageIndex(), param.getPageSize());
        Wrapper<TbUser> wrapper = new QueryWrapper<>(param);
        return new CommonResult(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message(), page(page, wrapper));
    }

}
