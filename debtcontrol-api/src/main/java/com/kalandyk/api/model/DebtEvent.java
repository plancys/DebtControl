package com.kalandyk.api.model;

import java.util.Date;

/**
 * Created by kamil on 12/29/13.
 */
public class DebtEvent extends AbstractModel{

    private DebtEventType eventType;

    private User eventCreator;

    private Date creationDate;

    public DebtEvent(DebtEventType eventType, User eventCreator){
        this.eventCreator = eventCreator;
        this.eventType = eventType;
    }

    public DebtEvent(){

    }

    public DebtEventType getEventType() {
        return eventType;
    }

    public void setEventType(DebtEventType eventType) {
        this.eventType = eventType;
    }

    public User getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(User eventCreator) {
        this.eventCreator = eventCreator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
