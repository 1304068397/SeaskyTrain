package com.king.train.config.druid;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidStatViewServletConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidWebStatFilterConfiguration;
import com.alibaba.druid.support.http.StatViewServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * spring-boot 特殊处理：大多数配置都在 application.properties 文件中完成，简化了数据库配置
 * JPA + MongoDB 部分自定义配置
 * <pre>
 * Spring Data JPA 配置
 *   参考配置：https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 *   QueryDSL
 *     参考配置：http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#jpa_integration
 * Spring Data MongoDB 配置, AbstractMongoConfiguration 已经默认实现了 mongoTemplate和mongoFactoryBean
 *    参考配置：https://docs.spring.io/spring-data/mongodb/docs/2.0.0.RC3/reference/html/
 *    QueryDSL
 *      参考配置： http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#mongodb_integration
 * 分库分表及多数据源也可以使用： Sharding-JDBC
 *    参考配置：https://shardingsphere.apache.org/document/current/cn/overview/
 *
 * 实现数据监控：增加DruidConfig的配置类并增加配置
 *
 * druidServlet：注册一Servlet应用，页面地址相对地址为druid，页面输入2个参数，登录名和密码，交由Druid验证。
 * statFilter：加载配置文件中的监控配置属性到StatFilter
 *
 * @author 谢长春 2019/1/23
 *
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.datasource.druid.web-stat-filter.enabled", havingValue = "true")
@Import({DruidWebStatFilterConfiguration.class, DruidStatViewServletConfiguration.class})
public class DruidConfig {

    /***  主要实现WEB监控的配置处理*
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        // 现在要进行druid监控的配置处理操作
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 控制台管理用户名
        servletRegistrationBean.addInitParameter("loginUsername", "druidadmin");
        // 控制台管理密码
        servletRegistrationBean.addInitParameter("loginPassword", "12345");
        return servletRegistrationBean ;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.filter.stat")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.filter.stat", name = "enabled")
    @ConditionalOnMissingBean
    public StatFilter statFilter() {
        return new StatFilter();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.stat-view-servlet")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.stat-view-servlet", name = "enabled")
    @ConditionalOnMissingBean
    public DruidStatProperties.StatViewServlet statViewServlet() {
        return new DruidStatProperties.StatViewServlet();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.web-stat-filter")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.web-stat-filter", name = "enabled")
    @ConditionalOnMissingBean
    public DruidStatProperties.WebStatFilter webStatFilter() {
        return new DruidStatProperties.WebStatFilter();
    }

    @Bean
    public DruidStatProperties druidStatProperties(
            DruidStatProperties.StatViewServlet statViewServlet,
            DruidStatProperties.WebStatFilter webStatFilter
    ) {
        final DruidStatProperties properties = new DruidStatProperties();
        properties.setStatViewServlet(statViewServlet);
        properties.setWebStatFilter(webStatFilter);
        return properties;
    }
}