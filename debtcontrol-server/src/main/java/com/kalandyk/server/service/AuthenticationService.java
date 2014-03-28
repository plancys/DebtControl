package com.kalandyk.server.service;

import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kamil on 3/21/14.
 */
@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity authenticateUser(String login, String password){
        //TODO: add real auth
        return userRepository.findByLogin(login);
    }

}
