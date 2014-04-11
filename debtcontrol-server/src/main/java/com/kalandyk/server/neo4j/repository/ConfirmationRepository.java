package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.DebtConfirmationEntity;

public interface ConfirmationRepository extends GraphRepository<DebtConfirmationEntity> {

    DebtConfirmationEntity save(DebtConfirmationEntity confirmationEntity);

    DebtConfirmationEntity findOne(Long id);
}
