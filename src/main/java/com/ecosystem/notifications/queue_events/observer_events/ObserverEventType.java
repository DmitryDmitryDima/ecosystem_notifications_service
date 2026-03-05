package com.ecosystem.notifications.queue_events.observer_events;

public enum ObserverEventType {


    PROJECT_SUB("project_sub"), PROJECT_UNSUB("project_unsub");


    private final String value;

    ObserverEventType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }


}
