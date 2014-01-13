package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.ConfirmationEntity;
import com.kalandyk.server.neo4j.entity.DebtEntity;

/**
 * Created by kamil on 1/13/14.
 */
public interface ConfirmationRepository extends GraphRepository<ConfirmationEntity> {

    ConfirmationEntity save(ConfirmationEntity confirmationEntity);

    ConfirmationEntity findOne(Long id);
}
