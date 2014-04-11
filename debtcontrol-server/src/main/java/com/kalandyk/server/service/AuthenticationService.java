package com.kalandyk.server.service;

import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by kamil on 3/21/14.
 */
@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName());
    }

    public Boolean authenticateUser(String login, String password) {
        UserEntity userEntity = userRepository.findByEmail(login);
        if (userEntity == null) {
            return false;
        }
        return true;
    }
}
