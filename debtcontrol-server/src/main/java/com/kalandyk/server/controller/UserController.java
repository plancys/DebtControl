package com.kalandyk.server.controller;

import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "getUserByName/{username}", method = RequestMethod.GET)
    @ResponseBody
    public User getUserByUsername(@PathVariable String username){
        return userService.findUserByUsername(username);
    }

    @RequestMapping(value = "getUsersFriends/{username}", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsersFriends(@PathVariable String username){
        return userService.findUsersFriends(username);
    }


}
