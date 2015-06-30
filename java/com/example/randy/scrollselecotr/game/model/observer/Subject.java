package com.example.randy.scrollselecotr.game.model.observer;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by randy on 15-6-6.
 */
public abstract class Subject {
    protected LinkedList<Observer> mObservers=new LinkedList<Observer>();
    public void attach(Observer observer) {
        this.mObservers.add(observer);
    }
    public void detach(Observer observer) {
        this.mObservers.remove(observer);
    }
    public abstract void  notifyObserver();
}
