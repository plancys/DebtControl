package com.kalandyk.server.neo4j.entity;

import com.kalandyk.api.model.User;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by kamil on 1/4/14.
 */
@NodeEntity
public class UserEntity extends AbstractEntity {

    public static final String USER_FRIENDSHIP_RELATION = "KNOWS";
    public static final String USER_FRIENDSHIP_REQUEST = "REQUEST_FRIENDSHIP";

    //@Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String login;

    private String name;

    private String forename;

    private String password;

    @RelatedTo(type = USER_FRIENDSHIP_RELATION, elementClass = UserEntity.class, direction = Direction.BOTH)
    //@Fetch
    private Set<UserEntity> friends;

    @RelatedTo(type = USER_FRIENDSHIP_REQUEST, elementClass = UserEntity.class, direction = Direction.INCOMING)
    @Fetch
    private Set<UserEntity> addingToFriendsRequests;


    public UserEntity() {

    }

    public UserEntity(User user) {
        setEmail(user.getEmail());
        setLogin(user.getLogin());
        setPassword(user.getPassword());
        setName(user.getName());
        setForename(user.getForename());
        //setFriends(convertFriendsFromUser(user.getFriends()));
    }

    private Set<UserEntity> convertFriendsFromUser(Set<User> friends) {
        if(friends == null){
            return new HashSet<UserEntity>();
        }
        Set<UserEntity> userFriends = new HashSet<UserEntity>();
        Iterator<User> iterator = friends.iterator();
        while (iterator.hasNext()) {
            userFriends.add(new UserEntity(iterator.next()));
        }
        return userFriends;
    }

    private Set<User> convertFriendsFromUserEntity(Set<UserEntity> friends) {
        if(friends == null){
            return new HashSet<User>();
        }
        Set<User> userFriends = new HashSet<User>();
        Iterator<UserEntity> iterator = friends.iterator();
        while (iterator.hasNext()) {
            userFriends.add(iterator.next().toUserModelWithoutFriends());
        }
        return userFriends;
    }

    public UserEntity(String email, String login, String password) {
        this.email = email;
        this.password = password;
        this.login = login;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserEntity> getFriends() {
        return friends;
    }

    public void setFriends(Set<UserEntity> friends) {
        this.friends = friends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public Set<UserEntity> getAddingToFriendsRequests() {
        if(addingToFriendsRequests == null){
            addingToFriendsRequests = new HashSet<UserEntity>();
        }
        return addingToFriendsRequests;
    }

    public void setAddingToFriendsRequests(Set<UserEntity> addingToFriendsRequests) {
        this.addingToFriendsRequests = addingToFriendsRequests;
    }

    private User toUserModelWithoutFriends(){
            User user = new User();
            user.setLogin(this.login);
            user.setEmail(this.email);
            user.setPassword(this.password);
            user.setName(this.name);
            user.setForename(this.forename);

            return user;

    }

    public User toUserModel() {
        User user = new User();
        user.setId(getId());
        user.setLogin(getLogin());
        user.setEmail(getEmail());
        user.setPassword(getPassword());
        user.setName(getName());
        user.setForename(getForename());
        //user.setFriends(convertFriendsFromUserEntity(this.friends));
        return user;
    }
}
