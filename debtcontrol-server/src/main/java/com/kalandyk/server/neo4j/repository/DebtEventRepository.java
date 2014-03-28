package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.DebtEventEntity;

public interface DebtEventRepository extends GraphRepository<DebtEventEntity> {

    DebtEventEntity save(DebtEventEntity debtEventEntity);

    DebtEventEntity findOne(Long id);
}
