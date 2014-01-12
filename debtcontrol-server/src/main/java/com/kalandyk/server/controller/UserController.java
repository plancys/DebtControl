package com.kalandyk.server.controller;

import com.kalandyk.api.model.FriendshipRequest;
import com.kalandyk.api.model.User;
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

import java.util.List;

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

    @RequestMapping(value = "getUserByName/{username}", method = RequestMethod.GET)
    @ResponseBody
    public User getUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    @RequestMapping(value = "getUsersFriends/{username}", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsersFriends(@PathVariable String username) {
        return userService.findUsersFriends(username);
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


}
