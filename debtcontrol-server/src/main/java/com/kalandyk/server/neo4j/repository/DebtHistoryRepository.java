package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.DebtHistoryEntity;

/**
 * Created by kamil on 1/17/14.
 */
public interface DebtHistoryRepository extends GraphRepository<DebtHistoryEntity> {
    DebtHistoryEntity save(DebtHistoryEntity historyEntity);
}
