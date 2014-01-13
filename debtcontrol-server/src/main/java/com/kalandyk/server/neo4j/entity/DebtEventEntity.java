package com.kalandyk.server.neo4j.entity;

import com.kalandyk.api.model.DebtEventType;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.Date;

/**
 * Created by kamil on 1/13/14.
 */

@NodeEntity
public class DebtEventEntity extends AbstractEntity {
    public static final String EVENT_CREATOR_RELATION = "CREATOR";

    private DebtEventType eventType;

    @RelatedTo(type = EVENT_CREATOR_RELATION, elementClass = UserEntity.class, direction = Direction.OUTGOING)
    private UserEntity eventCreator;

    private Date creationTime;

    public DebtEventType getEventType() {
        return eventType;
    }

    public void setEventType(DebtEventType eventType) {
        this.eventType = eventType;
    }

    public UserEntity getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(UserEntity eventCreator) {
        this.eventCreator = eventCreator;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
