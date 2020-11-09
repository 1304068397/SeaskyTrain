package com.king.train.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.king.train.entities.TbUser;
import com.king.train.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @PackageName:com.king.train.config.shiro
 * @ClassName:CustomRealm
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/9 10:36
 */
@Component
public class CustomRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        TbUser user = (TbUser)principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //以下权限信息，实际场景中应从数据库获得
        Set<String> perms = new HashSet<>(0);
        perms.add("createRight");
        Set<String> roles = new HashSet<>(0);
        roles.add("queryRole");

        authorizationInfo.setStringPermissions(perms);
        authorizationInfo.setRoles(roles);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取到用户界面输入的用户名和密码
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        //2.获取用户出入的用户名和密码
        String username = (String)upToken.getPrincipal();
        String password = new String(upToken.getPassword());
        //3.根据用户名查询用户对象
        TbUser user = userService.getOne(new QueryWrapper<>(TbUser.builder().userName(username).build()));
        if(user==null) {
            throw new AuthenticationException("用户名不正确！");
        }

        if(!user.getPassword().equals(password)) {
            throw new AuthenticationException("密码不正确！");
        }

        return new SimpleAuthenticationInfo(user,user.getPassword(), this.getName());
    }

}