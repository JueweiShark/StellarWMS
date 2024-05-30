package com.example.wmsspringbootproject.common.designmode;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@RequiredArgsConstructor
public class ConcreteProcess implements Process {
    private Map<ConcreteObserver, List<String>> subjectList;

    @Override
    public void addObserver(ConcreteObserver observer,List<String> filedList) {
        subjectList=new HashMap<>();
        if(subjectList.get(observer)==null){
            subjectList.put(observer,filedList);
        }else{
            subjectList.get(observer).addAll(filedList);
        }
    }

    @Override
    public void removeObserver(ConcreteObserver observer,List<String> filedList) {
        subjectList.get(observer).removeAll(filedList);
    }

    @Override
    public void notifyObservers(Object object) {
        int index=0;
        for (ConcreteObserver observer : subjectList.keySet()) {
            observer.update(subjectList.get(observer),object);
            index++;
        }
    }
}
