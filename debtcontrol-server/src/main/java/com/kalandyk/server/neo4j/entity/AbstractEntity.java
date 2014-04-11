package com.kalandyk.server.neo4j.entity;

import org.springframework.data.neo4j.annotation.GraphId;

import java.util.Date;

/**
 * Created by kamil on 1/4/14.
 */
public abstract class AbstractEntity {

    @GraphId
    private Long id;

    private Date timestamp;

    AbstractEntity(){
        timestamp = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        //DO NOTHING, for dozer mapping
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (id == null || obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        return id.equals(((AbstractEntity) obj).id);

    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public void updateTimestamp(){
        //TODO: implement Visitor or sth
        timestamp = new Date();
    }
}