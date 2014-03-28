package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.DebtEntity;

public interface DebtRepository extends GraphRepository<DebtEntity> {

    DebtEntity save(DebtEntity userEntityEntity);

    DebtEntity findOne(Long id);
}
