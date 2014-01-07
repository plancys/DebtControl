package com.kalandyk.server.neo4j.entity;

import org.springframework.data.neo4j.annotation.GraphId;

/**
 * Created by kamil on 1/4/14.
 */
public abstract class AbstractEntity {

    @GraphId
    private Long id;

    public Long getId() {
        return id;
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
}