package com.kalandyk.server.neo4j;

import com.kalandyk.server.neo4j.AbstractEntity;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Created by kamil on 1/4/14.
 */
@NodeEntity
public class User extends AbstractEntity {

    private String email;
    private String username;
    private String password;

    public User(){

    }

    public User(String email, String username, String password) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
