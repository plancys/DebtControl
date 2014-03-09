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

    private DebtEventType eventType;
    private Date time;

    public DebtEventEntity() {
        super();
    }

    public DebtEventType getEventType() {
        return eventType;
    }

    public void setEventType(DebtEventType eventType) {
        this.eventType = eventType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
