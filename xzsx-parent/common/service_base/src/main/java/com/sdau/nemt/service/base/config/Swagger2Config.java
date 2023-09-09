package com.sdau.nemt.service.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Date: 2022/7/16 17:13
 * @Author: 王辉
 * @Description: Swagger2Config配置类
 */
//@Profile("dev")
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket webApiConfig(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
        return docket;
    }

//    @Bean
//    public Docket testApiConfig(){
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .groupName("testApi")
//                .apiInfo(testApiInfo())
//                .select()
//                .paths(Predicates.and(PathSelectors.regex("/volunteer/.*")))
//                .build();
//        return docket;
//    }

    @Bean
    public Docket userApiConfig(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("userApi")
                .apiInfo(userApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/user/.*")))
                .build();
        return docket;
    }

    @Bean
    public Docket volunteerApiConfig(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("volunteerApi")
                .apiInfo(volunteerApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/volunteer/.*")))
                .build();
        return docket;
    }
    @Bean
    public Docket informationApiConfig(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("informationApi")
                .apiInfo(informationApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/information/.*")))
                .build();
        return docket;
    }


    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder().title("网站的API文档")
                .description("本文描述了高考志愿填报智能决策系统的api接口定义")
                .version("1.0")
                .contact(new Contact("buer","https://blog.csdn.net/qq_63451529?spm=1000.2115.3001.5343","buerjiguang@qq.com"))
                .build();
    }

//    private ApiInfo testApiInfo(){
//        return new ApiInfoBuilder().title("后台管理系统的API文档")
//                .description("本文描述了后台管理系统的api接口定义")
//                .version("1.0")
//                .contact(new Contact("buer","https://blog.csdn.net/qq_63451529?spm=1000.2115.3001.5343","buerjiguang@qq.com"))
//                .build();
//    }

    private ApiInfo userApiInfo(){
        return new ApiInfoBuilder().title("用户功能的API文档")
                .description("本文描述了用户功能的api接口定义")
                .version("1.0")
                .contact(new Contact("buer","https://blog.csdn.net/qq_63451529?spm=1000.2115.3001.5343","buerjiguang@qq.com"))
                .build();
    }
    private ApiInfo volunteerApiInfo(){
        return new ApiInfoBuilder().title("用户功能的API文档")
                .description("本文描述了院校志愿专业功能的api接口定义")
                .version("1.0")
                .contact(new Contact("buer","https://blog.csdn.net/qq_63451529?spm=1000.2115.3001.5343","buerjiguang@qq.com"))
                .build();
    }

    private ApiInfo informationApiInfo(){
        return new ApiInfoBuilder().title("用户功能的API文档")
                .description("本文描述了文章咨询和咨询师功能的api接口定义")
                .version("1.0")
                .contact(new Contact("buer","https://blog.csdn.net/qq_63451529?spm=1000.2115.3001.5343","buerjiguang@qq.com"))
                .build();
    }
}

