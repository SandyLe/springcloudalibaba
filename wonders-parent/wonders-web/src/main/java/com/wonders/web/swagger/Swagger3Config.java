package com.wonders.web.swagger;

import io.swagger.models.auth.In;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Field;
import java.util.*;


@EnableOpenApi
@Configuration
public class Swagger3Config implements WebMvcConfigurer {


    /**
     * 是否开启swagger，生产环境一般关闭，所以这里定义一个变量
     */
    @Value("${spring.swagger.enable:true}")
    private Boolean enable;

    /**
     * 邮箱地址
     */
    @Value("${spring.swagger.email:wuxingxing@wondersgroup.com}")
    private String email;

    /**
     * 管理员名称
     */
    @Value("${spring.swagger.sysName:wuxx}")
    private String sysName;

    /**
     * 接口调试地址
     */
    @Value("${spring.swagger.tryHost:http://127.0.0.1:8080}")
    private String tryHost;


    /**
     *
     * @param title 文档标题
     * @param description 文档描述
     * @param version 版本
     * @param route 路由
     * @param groupName 分组名称
     * @return
     */
    public Docket createRestRouteApi(String title,String description,String version,String route,String groupName) {
        return new Docket(DocumentationType.OAS_30).pathMapping("/")

                // 定义是否开启swagger，false为关闭，可以通过变量控制
                .enable(enable)

                // 将api的元信息设置为包含在json ResourceListing响应中。
                .apiInfo(apiInfo(title,description,version))
                .groupName(groupName)
                // 接口调试地址
                .host(tryHost)

                // 选择哪些接口作为swagger的doc发布
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant(route))
                .build()

                // 支持的通讯协议集合
                .protocols(newHashSet("https", "http"))

                // 授权信息设置，必要的header token等认证信息
                .securitySchemes(securitySchemes())

                // 授权信息全局应用
                .securityContexts(securityContexts());
    }

    /**
     *
     * @param title 文档标题
     * @param description 文档描述
     * @param version 版本
     * @param packagePath 包路径
     * @param groupName 分组名称
     * @return
     */
    public Docket createRestPackageApi(String title,String description,String version,String packagePath,String groupName) {
        return new Docket(DocumentationType.OAS_30).pathMapping("/")

                // 定义是否开启swagger，false为关闭，可以通过变量控制
                .enable(enable)

                // 将api的元信息设置为包含在json ResourceListing响应中。
                .apiInfo(apiInfo(title,description,version))
                .groupName(groupName)
                // 接口调试地址
                .host(tryHost)

                // 选择哪些接口作为swagger的doc发布
                .select()
                .apis(RequestHandlerSelectors.basePackage(packagePath))
                .paths(PathSelectors.any())
                .build()

                // 支持的通讯协议集合
                .protocols(newHashSet("https", "http"))

                // 授权信息设置，必要的header token等认证信息
                .securitySchemes(securitySchemes())

                // 授权信息全局应用
                .securityContexts(securityContexts());
    }


    /**
     * api信息
     *
     * @param name        标题
     * @param description 描述
     * @param version     版本
     * @return
     */
    private ApiInfo apiInfo(String name, String description, String version) {
        return new ApiInfoBuilder()
                .title(name)
                .description(description)
                .contact(new Contact(sysName, null, email))
                .version("Application Version: " + version + ", Spring Boot Version: " + SpringBootVersion.getVersion())
                .build();
    }

    /**
     * 设置授权信息
     */
    private List<SecurityScheme> securitySchemes() {
        ApiKey apiKey = new ApiKey("X-Auth-Token", "X-Auth-Token", In.HEADER.toValue());
        return Collections.singletonList(apiKey);
    }

    /**
     * 授权信息全局应用
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(Collections.singletonList(new SecurityReference("X-Auth-Token", new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")})))
                        .build()
        );
    }

    @SafeVarargs
    private final <T> Set<T> newHashSet(T... ts) {
        if (ts.length > 0) {
            return new LinkedHashSet<>(Arrays.asList(ts));
        }
        return null;
    }

    /**
     * 通用拦截器排除swagger设置，所有拦截器都会自动加swagger相关的资源排除信息
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        try {
            Field registrationsField = FieldUtils.getField(InterceptorRegistry.class, "registrations", true);
            List<InterceptorRegistration> registrations = (List<InterceptorRegistration>) ReflectionUtils.getField(registrationsField, registry);
            if (registrations != null) {
                for (InterceptorRegistration interceptorRegistration : registrations) {
                    interceptorRegistration
                            .excludePathPatterns("/swagger**/**")
                            .excludePathPatterns("/webjars/**")
                            .excludePathPatterns("/v3/**")
                            .excludePathPatterns("/doc.html");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
