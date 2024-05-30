package com.example.wmsspringbootproject.common.designmode;
import com.example.wmsspringbootproject.common.Annotation.Subject;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class FieldChangeAspect {
    private final ConcreteProcess concreteProcess;

    @Pointcut("@annotation(com.example.wmsspringbootproject.common.Annotation.Subject)")
    public void fieldChangePointcut() {

    }

    @Before("fieldChangePointcut()")
    public void beforeFieldChange(JoinPoint joinPoint) {
        if(concreteProcess.getSubjectList()!=null){
            concreteProcess.getSubjectList().clear();
        }
        MethodSignature methodSignature=(MethodSignature)joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        Subject subject=method.getAnnotation(Subject.class);
        String fieldName = subject.filedName(); // 获取字段名
        Class<?> val=subject.observer();
        ConcreteObserver observer=new ConcreteObserver();
        observer.setObserverClass(val);
        List<String> subjects=new ArrayList<>();
        subjects.add(fieldName);
        concreteProcess.addObserver(observer,subjects);
    }

    @AfterReturning(pointcut = "fieldChangePointcut() && args(newVal)",
            returning = "result", argNames = "joinPoint,newVal,result")
    public void afterFieldChange(JoinPoint joinPoint, Object newVal, Object result) {
        concreteProcess.notifyObservers(newVal);
    }
}

