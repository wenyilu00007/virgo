package com.hoau.virgo.swagger;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 刘德云
 * @version V1.0
 * @Title: SwaggerConfiguration
 * @Package com.hoau.leo.config.web
 * @date 2017/8/3
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "virgo.swagger", name = "enable", matchIfMissing = true)
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${spring.application.name}")
    String applicationName;
    @Value("${server.context-path}")
    String contextPath;
    @Autowired
    SwaggerProperties swaggerProperties;

    @Bean
    public Docket swagger2Api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(applicationName)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                //控制哪些接口暴露给Swagger
                .select().paths(Predicates.or(PathSelectors.regex(swaggerProperties.getPath()+"/.*")))
                .build()
                //用来创建该Api的基本信息(这些基本信息会展现在文档页面中)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                swaggerProperties.getTitle(),//标题
                swaggerProperties.getDescription(),//副标题
                swaggerProperties.getVersion(),//版本
                swaggerProperties.getTermsOfServiceUrl(), //条款
                swaggerProperties.getContactName(),//作者
                swaggerProperties.getLicense(),//链接显示文字
                swaggerProperties.getLicenseUrl()//网站链接
        );
        return apiInfo;
    }

}
