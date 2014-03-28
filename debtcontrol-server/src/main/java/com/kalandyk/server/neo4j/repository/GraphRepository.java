package com.kalandyk.server.neo4j.repository;

import org.springframework.data.neo4j.repository.CRUDRepository;
import org.springframework.data.neo4j.repository.IndexRepository;
import org.springframework.data.neo4j.repository.TraversalRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GraphRepository<T> extends CRUDRepository<T>, IndexRepository<T>, TraversalRepository<T> {
}
