package com.example.wmsspringbootproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

@SpringBootTest
class WmsSpringbootProjectApplicationTests {

    @Test
    void contextLoads() {
        String message = "Hello World!";
        for (Field declaredField : message.getClass().getDeclaredFields()) {
            System.out.println(declaredField.getName());
        }
    }

}
