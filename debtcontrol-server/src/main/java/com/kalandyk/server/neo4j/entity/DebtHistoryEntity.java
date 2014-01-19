package com.kalandyk.server.neo4j.entity;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kamil on 1/17/14.
 */
@NodeEntity
public class DebtHistoryEntity extends AbstractEntity {

    public static final String EXIST_EVENT = "EXIST_EVENT";

    @RelatedTo(type = EXIST_EVENT, direction = Direction.OUTGOING, elementClass = DebtEventEntity.class)
    @Fetch
    private Set<DebtEventEntity> events;

    public DebtHistoryEntity() {
        super();
    }

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
