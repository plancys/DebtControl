package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.ConfirmationEntity;
import com.kalandyk.server.neo4j.entity.DebtEventEntity;

/**
 * Created by kamil on 1/17/14.
 */
public interface DebtEventRepository extends GraphRepository<DebtEventEntity> {

    DebtEventEntity save(DebtEventEntity debtEventEntity);

    DebtEventEntity findOne(Long id);
}
