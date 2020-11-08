package com.king.train.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @PackageName:com.king.train.entities
 * @ClassName:tbUser
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/8 19:18
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    //@Value注解从配置文件取值
    @Value("${swagger.enable}")
    private Boolean swaggerEnale;

    @Bean
    public Docket api() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnale)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("seasky_restapi_v1")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(Timestamp.class, String.class)
                .directModelSubstitute(Date.class, String.class)
                .useDefaultResponseMessages(false);
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Train-API")     // 文档标题
                .contact(new Contact("King", "", ""))   //联系人信息
                .description("Train-API")      //描述
                .version("1.0")     //文档版本号
                .termsOfServiceUrl("http://localhost")     //网站地址
                .build();
    }

}
