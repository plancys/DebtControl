package com.kalandyk.api.model;

/**
 * Created by kamil on 12/29/13.
 */
public class DebtEvent {

    private DebtEventType eventType;

    private User eventAuthor;

    public DebtEvent(DebtEventType eventType, User eventAuthor){
        this.eventAuthor = eventAuthor;
        this.eventType = eventType;
    }

    public DebtEventType getEventType() {
        return eventType;
    }

    public void setEventType(DebtEventType eventType) {
        this.eventType = eventType;
    }

    public User getEventAuthor() {
        return eventAuthor;
    }

    public void setEventAuthor(User eventAuthor) {
        this.eventAuthor = eventAuthor;
    }
}
