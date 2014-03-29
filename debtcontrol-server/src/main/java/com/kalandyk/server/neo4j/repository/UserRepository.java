package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.UserEntity;
import org.springframework.data.neo4j.annotation.Query;

public interface UserRepository extends GraphRepository<UserEntity> {

    UserEntity save(UserEntity userEntityEntity);

    UserEntity findOne(Long id);

    UserEntity findByEmail(String name);

    @Query("START user={0} MATCH user<-[:HAS_DEBTOR]-debt RETURN sum(debt.amount)")
    Long getUsersDebt(UserEntity userEntity);

}
