package com.kalandyk.server.service;

import com.kalandyk.server.neo4j.entity.UserEntity;

import static org.junit.Assert.*;

public class DebtTestPreparation {

    public static final String USER_A_USERNAME = "A@A.pl";
    public static final String USER_B_USERNAME = "B@B.pl";

    private UserEntity userA;
    private UserEntity userB;


    public void prepareTwoUsers(UserService userService) {
        UserEntity userA = new UserEntity();
        userA.setEmail(USER_A_USERNAME);

        UserEntity userB = new UserEntity();
        userB.setEmail(USER_B_USERNAME);
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
