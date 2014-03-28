package com.kalandyk.server.service;

import com.kalandyk.server.neo4j.entity.UserEntity;

import static org.junit.Assert.*;

/**
 * Created by kamil on 3/20/14.
 */
public class DebtTestPreparation {

    public static final String USER_A_USERNAME = "A";
    public static final String USER_B_USERNAME = "B";

    private UserEntity userA;
    private UserEntity userB;


    public void prepareTwoUsers(UserService userService) {
        UserEntity userA = new UserEntity();
        userA.setLogin(USER_A_USERNAME);

        UserEntity userB = new UserEntity();
        userB.setLogin(USER_B_USERNAME);
        try {
            this.userA = userService.createUser(userA);
            this.userB = userService.createUser(userB);
        } catch (Exception e) {
            fail("User creation error: " + e.toString());
        }
    }

    public UserEntity getUserA() {
        return userA;
    }

    public UserEntity getUserB() {
        return userB;
    }
}
