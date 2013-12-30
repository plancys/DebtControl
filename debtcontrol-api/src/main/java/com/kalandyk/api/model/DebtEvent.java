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

}
