package com.kalandyk.server.neo4j.repository;

import com.kalandyk.server.neo4j.entity.UserEntity;

/**
 * Created by kamil on 1/4/14.
 */
public interface UserRepository extends GraphRepository<UserEntity> {

    UserEntity save(UserEntity userEntityEntity);

    UserEntity findOne(Long id);

    UserEntity findByUsername(String name);

}
