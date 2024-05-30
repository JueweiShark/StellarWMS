package com.example.wmsspringbootproject.common.designmode;

import com.example.wmsspringbootproject.common.Annotation.Listener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

@Data
@Slf4j
public class ConcreteObserver implements Observer{
    private String name;
    private Class<?> observerClass;
    @Override
    public void update(List<String> fieldList,Object object) {
        for (Method declaredMethod : observerClass.getDeclaredMethods()) {

                try{
                    Listener listener=declaredMethod.getAnnotation(Listener.class);
                    if(listener!=null){
                        String[] arrayValue=listener.filedName();
                        declaredMethod.setAccessible(true);
                        // 获取参数类型
                        Parameter[] parameters = declaredMethod.getParameters();
                        List<String> nameList=Arrays.stream(parameters).map(Parameter::getName).toList();
                        if(arrayValue.length==1 && arrayValue[0].isEmpty()){
                            arrayValue=nameList.toArray(new String[nameList.size()]);
                        }
                        if(Arrays.stream(arrayValue).toList().contains(fieldList.get(0))) {
                            // 如果方法接受参数
                            if (parameters.length > 0) {
                                // 构造参数数组
                                Object[] arguments =new Object[parameters.length];
                                //通过反射拿到值
                                Field[] fields = object.getClass().getDeclaredFields();
                                for (int i=0; i<fields.length; i++) {
                                    fields[i].setAccessible(true);
                                    String val=String.valueOf(fields[i].get(object));
                                    if(nameList.contains(fields[i].getName()) && i<parameters.length){
                                        arguments[nameList.indexOf(fields[i].getName())]=val;
                                    }
                                }
                                declaredMethod.invoke(observerClass.getDeclaredConstructor().newInstance(), arguments);
                            } else {
                                // 方法不接受参数
                                declaredMethod.invoke(observerClass.getDeclaredConstructor().newInstance());
                            }
                        }
                    }else{
                        log.warn("未添加@Listener注解");
                    }
                }catch (Exception e){
                    log.info(e.getMessage());
                }
        }
    }
}
