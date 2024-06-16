
package com.example.wmsspringbootproject.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


import java.util.HashMap;
import java.util.Map;

/**
 * @author 初秋
 * @since 2024-05-25
 * @version 1.0
 */
@Configuration
public class Knife4jConfig {

    private static final Map<String,Object> extensions = new HashMap<>();

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                // 接口文档标题
                .info(new Info().title("仓库管理系统")
                        // 接口文档描述
                        .description("仓库管理系统接口文档")
                        // 接口文档版本
                        .version("v1.0")
                        // 开发者联系方式
                        .contact(new Contact().name("Author01").url("https://gitee.com/initial_autumn")
                                .email("13339681512@163.com").extensions(extensions)))
                .externalDocs(new ExternalDocumentation()
                        // 额外补充说明
                        .description("Github example code")
                        // 额外补充链接
                        .url("https://github.com/Flying9001/springBootDemo/demo-knife4j-openapi3"))
                .components(new Components()
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION,new SecurityScheme()
                                .name(HttpHeaders.AUTHORIZATION)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .scheme("wms")
                                .bearerFormat("JWT")));
    }

    /**
     * 全局自定义扩展
     * <p>
     * 在OpenAPI规范中，Operation 是一个表示 API 端点（Endpoint）或操作的对象。
     * 每个路径（Path）对象可以包含一个或多个 Operation 对象，用于描述与该路径相关联的不同 HTTP 方法（例如 GET、POST、PUT 等）。
     */
    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> {
            // 全局添加鉴权参数
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((s, pathItem) -> {
                    // 登录接口/验证码不需要添加鉴权参数
                    if (s.equals("/login")) {
                        return;
                    }
                    // 接口添加鉴权参数
                    pathItem.readOperations()
                            .forEach(operation ->
                                    operation.addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                            );
                });
            }
        };
    }

}