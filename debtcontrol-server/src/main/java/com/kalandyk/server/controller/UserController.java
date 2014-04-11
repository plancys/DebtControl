package com.kalandyk.server.controller;

import com.kalandyk.api.model.User;
import com.kalandyk.api.model.wrapers.Friends;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.UserRepository;
import com.kalandyk.server.service.AuthenticationService;
import com.kalandyk.server.service.UserService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("secured/users")
@Transactional
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private Mapper mapper;

    //TODO: change this in android
    @RequestMapping(value = "getUserByEmail", method = RequestMethod.POST)
    @ResponseBody
    public User getUserByEmail(@RequestBody String email) {
        //TODO: figure out why we have additional " characters
        email = email.replace("\"", "");
        return userService.findUserByEmail(email);
    }

    @RequestMapping(value = "getUsersFriends", method = RequestMethod.GET)
    @ResponseBody
    public Friends getUsersFriends() {
        //TODO: create some abstract method for this + debt to yourself
        Friends friendsObj = new Friends();
        UserEntity authenticatedUser = authenticationService.getAuthenticatedUser();
        for (UserEntity userEntity : authenticatedUser.getFriends()) {
            UserEntity fetchedUser = userRepository.findOne(userEntity.getId());
            fetchedUser.setPassword(null);
            friendsObj.getFriends().add(mapToDTO(fetchedUser));
        }
        return friendsObj;
    }

    @RequestMapping(value = "addFriend", method = RequestMethod.POST)
    @ResponseBody
    //TODO: change in android from Boolean to User, change argument to User
    public User createFriendshipRequest(@RequestBody User friend) throws DebtControlException {
        UserEntity user = userService
                .createAddingToFriendRequest(mapToEntity(friend),
                        authenticationService.getAuthenticatedUser());
        return mapToDTO(user);
    }

    @RequestMapping(value = "acceptFriendshipRequest", method = RequestMethod.POST)
    @ResponseBody
    //TODO: change in android from Boolean to User , change arguments
    public User acceptFriendshipRequest(@RequestBody User acceptedFriend) throws DebtControlException {
        UserEntity approver = userService.makeDecisionRegardingFriendshipRequest(
                authenticationService.getAuthenticatedUser(),
                mapToEntity(acceptedFriend), true);
        return mapToDTO(approver);
    }

    @RequestMapping(value = "cancelFriendshipRequest", method = RequestMethod.POST)
    @ResponseBody
    //TODO: change in android from Boolean to User, , change arguments
    public User cancelFriendshipRequest(@RequestBody User acceptedFriend) throws DebtControlException {
        UserEntity approver = userService.makeDecisionRegardingFriendshipRequest(
                authenticationService.getAuthenticatedUser(),
                mapToEntity(acceptedFriend), false);
        return mapToDTO(approver);
    }

    //TODO: remove this
    @RequestMapping(value = "getUserData", method = RequestMethod.GET)
    @ResponseBody
    public User getUserData() throws DebtControlException {
        UserEntity authenticatedUser = authenticationService.getAuthenticatedUser();
        authenticatedUser.setPassword(null);
        return mapToDTO(authenticatedUser);
    }

    private UserEntity mapToEntity(User user) {
        return mapper.map(user, UserEntity.class);
    }

    private User mapToDTO(UserEntity approver) {
        return mapper.map(approver, User.class);
    }


}
