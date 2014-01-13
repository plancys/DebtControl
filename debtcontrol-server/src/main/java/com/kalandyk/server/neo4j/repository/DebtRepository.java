package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.DebtEntity;

/**
 * Created by kamil on 1/13/14.
 */
public interface DebtRepository extends GraphRepository<DebtEntity> {

    DebtEntity save(DebtEntity userEntityEntity);

    DebtEntity findOne(Long id);
}
