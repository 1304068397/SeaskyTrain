package com.king.train.config.shiro;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 *
 */

@Configuration
public class ShiroConfig {

    @Bean
    public DefaultWebSecurityManager securityManager(AuthorizingRealm realm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm);
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Autowired @Lazy DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factory = new ShiroFilterFactoryBean();
        factory.setLoginUrl("/");
        factory.setSecurityManager(securityManager);

        //以下内容，实际场景中应从配置文件获得
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/doc.html**", "anon");
        filterChainDefinitionMap.put("/**/login", "anon");
        filterChainDefinitionMap.put("/**/index", "anon");
        filterChainDefinitionMap.put("/**/UserCtrl001/**", "authc");

        factory.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factory;
    }

}