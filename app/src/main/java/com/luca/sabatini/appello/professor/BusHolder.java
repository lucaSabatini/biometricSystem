package com.luca.sabatini.appello.professor;

import org.greenrobot.eventbus.EventBus;

public class BusHolder {

    private static EventBus eventBus;

    public static EventBus getInstance() {
        if (eventBus == null) {
            eventBus = new EventBus();
        }
        return eventBus;
    }

    private BusHolder() {
    }
}