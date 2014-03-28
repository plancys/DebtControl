package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.DebtHistoryEntity;

public interface DebtHistoryRepository extends GraphRepository<DebtHistoryEntity> {
    DebtHistoryEntity save(DebtHistoryEntity historyEntity);
}
