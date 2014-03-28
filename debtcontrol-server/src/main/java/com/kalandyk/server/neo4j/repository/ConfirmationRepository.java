package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.ConfirmationEntity;

public interface ConfirmationRepository extends GraphRepository<ConfirmationEntity> {

    ConfirmationEntity save(ConfirmationEntity confirmationEntity);

    ConfirmationEntity findOne(Long id);
}
