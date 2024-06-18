package com.example.wmsspringbootproject;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.example.wmsspringbootproject.model.entity.Users;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
@EnableSpringUtil
@EnableAsync
public class WmsSpringbootProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmsSpringbootProjectApplication.class, args);
        System.out.println("============================================================================");
    }

}
