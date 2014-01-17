package com.kalandyk.server.neo4j.entity;

import com.kalandyk.api.model.DebtEvent;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kamil on 1/17/14.
 */
@NodeEntity
public class DebtHistoryEntity extends AbstractEntity {

    public static final String EXIST_EVENT = "EXIST_EVENT";

    @RelatedTo(type = EXIST_EVENT, direction = Direction.OUTGOING, elementClass = DebtEventEntity.class)
    private Set<DebtEventEntity> events;

    public Set<DebtEventEntity> getEvents() {
        return events;
    }

    public void setEvents(Set<DebtEventEntity> events) {
        this.events = events;
    }

    public void addEvent(DebtEventEntity debtEventEntity){
        if(events == null){
            events = new HashSet<DebtEventEntity>();
        }
        events.add(debtEventEntity);
    }
}
