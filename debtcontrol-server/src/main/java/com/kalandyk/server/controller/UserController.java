package com.kalandyk.server.controller;

import com.kalandyk.api.model.FriendshipRequest;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;
import com.kalandyk.api.model.wrapers.Friends;
import com.kalandyk.server.neo4j.repository.UserRepository;
import com.kalandyk.server.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by kamil on 1/5/14.
 */

@Controller
@RequestMapping("/users")
@Transactional
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @RequestMapping(value = "getUserByLogin/{login}", method = RequestMethod.GET)
    @ResponseBody
    public User getUserByUsername(@PathVariable String login) {
        return userService.findUserByLogin(login);
    }

    @RequestMapping(value = "getUsersFriends/{login}", method = RequestMethod.GET)
    @ResponseBody
    public Friends getUsersFriends(@PathVariable String login) {
        Set<User> userFriendsByLogin = userService.findUserFriendsByLogin(login);
        //TODO: create some abstract method for this
        List<User> friends = new ArrayList<User>();
        for(User user: userFriendsByLogin){
            friends.add(user);
        }
        Friends friendsObj = new Friends();
        friendsObj.setFriends(friends);

        return friendsObj;
    }

    @RequestMapping(value = "createUser", method = RequestMethod.POST)
    @ResponseBody
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @RequestMapping(value = "addFriend", method = RequestMethod.POST)
    @ResponseBody
    public boolean createFriendshipRequest(@RequestBody FriendshipRequest request) {
        return userService.createAddingToFriendRequest(request.getTarget(), request.getRequester());
    }

    @RequestMapping(value = "acceptFriendshipRequest", method = RequestMethod.POST)
    @ResponseBody
    public boolean acceptFriendshipRequest(@RequestBody FriendshipRequest request) {
        return userService.acceptAddingToFriendRequest(request.getTarget(), request.getRequester());
    }

    @RequestMapping(value = "cancelFriendshipRequest", method = RequestMethod.POST)
    @ResponseBody
    public boolean cancelFriendshipRequest(@RequestBody FriendshipRequest request) {
        return userService.cancelAddingToFriendRequest(request.getTarget(), request.getRequester());
    }

    @RequestMapping(value = "getUserDebtSum", method = RequestMethod.GET)
    @ResponseBody
    public Long getUserDebt() {
        return userRepository.getUsersDebt(userRepository.findByLogin("test1"));
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public User login(@RequestBody UserCredentials credentials) {
        return userService.authenticateUser(credentials.getLogin(), credentials.getPassword());
    }


}
