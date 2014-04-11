package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.UserEntity;
import org.springframework.data.neo4j.annotation.Query;

public interface UserRepository extends GraphRepository<UserEntity> {

    UserEntity save(UserEntity userEntityEntity);

    UserEntity findOne(Long id);

    UserEntity findByEmail(String email);
}
