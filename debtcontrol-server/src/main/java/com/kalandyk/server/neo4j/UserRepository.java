package com.kalandyk.server.neo4j;

import org.springframework.stereotype.Repository;

/**
 * Created by kamil on 1/4/14.
 */
public interface UserRepository extends GraphRepository<User> {

      User save (User userEntity);
//
//    User findOne(Long id);
//
//    User findByName(String name);

}
