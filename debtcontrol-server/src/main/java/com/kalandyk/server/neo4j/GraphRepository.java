package com.kalandyk.server.neo4j;

import org.springframework.data.neo4j.repository.CRUDRepository;
import org.springframework.data.neo4j.repository.IndexRepository;
import org.springframework.data.neo4j.repository.TraversalRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by kamil on 1/4/14.
 */
@NoRepositoryBean
public interface GraphRepository<T> extends CRUDRepository<T>, IndexRepository<T>, TraversalRepository<T> {
}
