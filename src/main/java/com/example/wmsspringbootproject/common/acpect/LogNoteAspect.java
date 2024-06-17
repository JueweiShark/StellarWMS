package com.example.wmsspringbootproject.common.acpect;


import com.alibaba.fastjson.JSON;
import com.example.wmsspringbootproject.common.Annotation.LogNote;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import cn.hutool.*;
/**
 * <p>
 * 日志记录 切面
 * </p>
 *
 * @author 初秋
 * @since 2024-06-09
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LogNoteAspect {

    private static Long records=0L;

    @Pointcut("@annotation(com.example.wmsspringbootproject.common.Annotation.LogNote)")
    public void logNotePoint() {

    }

    @Around(value = "logNotePoint() && @annotation(note)")
    public Object aroundLogNote(ProceedingJoinPoint joinPoint,LogNote note) throws IllegalAccessException {
        //执行的方法
        MethodSignature MethodSignature = (MethodSignature) joinPoint.getSignature();
        Method method=MethodSignature.getMethod();

        records++;
        log.info("-----------------------------------------LOG 第"+records+"条记录-----------------------------------------");

        log.info("执行的方法是："+method.getName());
        log.info("description："+note.description());
        log.info("方法的包路径："+method.getDeclaringClass().getPackage().getName()+"."+method.getDeclaringClass().getName());

        Object[] objects=joinPoint.getArgs();

        //方法的参数
        Parameter[] parameters=method.getParameters();
        StringBuilder names= new StringBuilder("[ ");
        StringBuilder values=new StringBuilder("[ ");
        StringBuilder types=new StringBuilder("[ ");
        for (Parameter parameter : parameters) {
            if(names.length()>2){
                names.append(", ");
            }
            names.append(parameter.getName());
        }
        for (Object object : objects) {
            Field[] fields=object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(!String.valueOf(field.get(object)).isEmpty()){
                    if(types.length()>2){
                        types.append(", ");
                    }
                    if(String.valueOf(field.getType()).contains(".")){
                        String type=String.valueOf(field.getType());
                        types.append(type.substring(type.lastIndexOf(".")+1));
                    }else{
                        types.append(field.getType());
                    }
                    if(values.length()>2){
                        values.append(", ");
                    }
                    values.append(field.getName());
                    values.append(": ");
                    values.append(field.get(object));
                }
            }
        }

        names.append(" ]");
        values.append(" ]");
        types.append(" ]");
        log.info("参数类型列表："+types);
        log.info("参数名列表："+names);
        log.info("参数值列表："+values);
        Object result=null;
        try{
            result=joinPoint.proceed();
            log.info("方法返回值为"+ JSON.toJSONString(result));
            log.info("-----------------------------------------LOG 第"+records+"条记录 结束-----------------------------------------");
            return result;
        }catch (Throwable e){
            log.error("异常信息："+e.getMessage());
        }
        log.info("-----------------------------------------LOG 第"+records+"条记录 结束-----------------------------------------");
        return result;
    }
}
