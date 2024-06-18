package com.example.wmsspringbootproject.config;

import com.example.wmsspringbootproject.core.security.exception.BusinessException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Order(Ordered.LOWEST_PRECEDENCE)
public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try{
            MutablePropertySources propertySources = environment.getPropertySources();
            Map<String,Object> map=new HashMap<>();
            map.put("im.enable", true);
            PropertySource<?> source = new MapPropertySource("sys_config",map);
            Object obj=source.getProperty("im.enable");
            propertySources.addFirst(source);
        }catch(Exception e){
            throw new BusinessException(e);
        }
    }
}
