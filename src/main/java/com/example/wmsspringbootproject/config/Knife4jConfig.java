
package com.example.wmsspringbootproject.config;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: openapi 界面配置
 * @Author: junqiang.lu
 * @Date: 2023/8/15
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
                        .url("https://github.com/Flying9001/springBootDemo/demo-knife4j-openapi3"));
    }

}