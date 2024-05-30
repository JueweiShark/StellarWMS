package com.example.wmsspringbootproject.common.designmode;

import java.util.List;

public interface Process {
    void addObserver(ConcreteObserver observer, List<String> filedList);

    void removeObserver(ConcreteObserver observer,List<String> filedList);

    void notifyObservers(Object object);
}
