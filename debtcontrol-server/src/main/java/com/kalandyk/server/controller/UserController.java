package com.kalandyk.server.controller;

import com.kalandyk.api.model.FriendshipRequest;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;
import com.kalandyk.api.model.wrapers.Friends;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.UserRepository;
import com.kalandyk.server.service.UserService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users")
@Transactional
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Mapper mapper;

    @RequestMapping(value = "getUserByLogin/{login}", method = RequestMethod.GET)
    @ResponseBody
    public User getUserByUsername(@PathVariable String login) {
        return userService.findUserByLogin(login);
    }

    @RequestMapping(value = "getUsersFriends/{login}", method = RequestMethod.GET)
    @ResponseBody
    public Friends getUsersFriends(@PathVariable String login) {
        //TODO: create some abstract method for this
        Friends friendsObj = new Friends();
        for (User user : userService.findUserFriendsByLogin(login)) {
            friendsObj.getFriends().add(user);
        }
        return friendsObj;
    }

    @RequestMapping(value = "createUser", method = RequestMethod.POST)
    @ResponseBody
    public User createUser(@RequestBody User user) throws DebtControlException {
        UserEntity userEntity = mapToEntity(user);
        userEntity = userService.createUser(userEntity);
        return mapToDTO(userEntity);
    }

    @RequestMapping(value = "addFriend", method = RequestMethod.POST)
    @ResponseBody
    //TODO: change in android from Boolean to User
    public User createFriendshipRequest(@RequestBody FriendshipRequest request) throws DebtControlException {
        UserEntity user = userService
                .createAddingToFriendRequest(mapToEntity(request.getTarget()), mapToEntity(request.getRequester()));
        return mapToDTO(user);
    }

    @RequestMapping(value = "acceptFriendshipRequest", method = RequestMethod.POST)
    @ResponseBody
    //TODO: change in android from Boolean to User
    public User acceptFriendshipRequest(@RequestBody FriendshipRequest request) throws DebtControlException {
        UserEntity requester = mapToEntity(request.getRequester());
        UserEntity approver = mapToEntity(request.getTarget());
        approver = userService.makeDecisionRegardingFriendshipRequest(approver, requester, true);
        return mapToDTO(approver);
    }

    @RequestMapping(value = "cancelFriendshipRequest", method = RequestMethod.POST)
    @ResponseBody
    //TODO: change in android from Boolean to User
    public User cancelFriendshipRequest(@RequestBody FriendshipRequest request) throws DebtControlException {
        UserEntity requester = mapToEntity(request.getRequester());
        UserEntity approver = mapToEntity(request.getTarget());
        approver = userService.makeDecisionRegardingFriendshipRequest(approver, requester, true);
        return mapToDTO(approver);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public User login(@RequestBody UserCredentials credentials) throws DebtControlException {
        return userService.authenticateUser(credentials.getLogin(), credentials.getPassword());
    }

    private UserEntity mapToEntity(User user) {
        return mapper.map(user, UserEntity.class);
    }

    private User mapToDTO(UserEntity approver) {
        return mapper.map(approver, User.class);
    }


}
