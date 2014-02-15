package com.kalandyk.api.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kamil on 12/2/13.
 */
public class User extends AbstractModel {

    private String email;

    private String login;

    private String name;

    private String forename;

    private String phoneNumber;

    private String password;

    //private Set<User> friends;

    public User() {
        //TODO: temporary preventing from null pointer exception
        this.login = "ExamplePerson";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

//    public Set<User> getFriends() {
//        return friends;
//    }
//
///    public void setFriends(Set<User> friends) {
//        this.friends = friends;
//
//    public void addFriend(User friend) {
//        if (friends == null) {
//            friends = new HashSet<User>();
//        }
//        friends.add(friend);
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", forename='" + forename + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
//                ", friends[Size]=" + friends.size() +
                '}';
    }
}
