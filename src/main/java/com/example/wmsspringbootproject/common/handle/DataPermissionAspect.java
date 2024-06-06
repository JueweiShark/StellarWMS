package com.example.wmsspringbootproject.common.handle;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
@Slf4j
public class DataPermissionAspect {
    private static List<Method> methods;

    @Pointcut("@annotation(com.example.wmsspringbootproject.common.Annotation.DataPermission)")
    public void dataPermissionPoint(){
    }

    @Before("dataPermissionPoint()")
    public static void beforeDataPermission(JoinPoint joinPoint){
        MethodSignature methodSignature=(MethodSignature)joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        methods=new ArrayList<>();
        methods.add(method);
    }

    public static List<Method> getMethods(){
        return methods;
    }

}
