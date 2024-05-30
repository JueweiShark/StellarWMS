package com.example.wmsspringbootproject;

import com.example.wmsspringbootproject.model.entity.Users;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WmsSpringbootProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmsSpringbootProjectApplication.class, args);
        System.out.println("============================================================================");
    }

}
