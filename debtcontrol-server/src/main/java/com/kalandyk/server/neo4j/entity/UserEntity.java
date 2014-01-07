package com.kalandyk.server.neo4j.entity;

import com.kalandyk.api.model.User;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Created by kamil on 1/4/14.
 */
@NodeEntity
public class UserEntity extends AbstractEntity {

    @Indexed(unique = true)
    private String email;

    //@Indexed(unique = true)
    private String username;

    private String password;

    public UserEntity(){

    }

    public UserEntity(User user){

    }

    public UserEntity(String email, String username, String password) {
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

    public User toUserModel() {
        User user = new User();
        user.setName(this.username);
        user.setEmail(this.email);



        return user;
    }
}
