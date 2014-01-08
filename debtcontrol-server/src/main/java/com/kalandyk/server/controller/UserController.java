package com.kalandyk.server.controller;

import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.UserRepository;
import com.kalandyk.server.service.UserService;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
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


    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public UserEntity saveExampleUser(){
//        UserEntity user1 = new UserEntity();
//        user1.setEmail("____!admidn1");
//        user1.setLogin("_admfidn1");
//        user1.setPassword("_afdmidn1");
//        user1 = userRepository.save(user1);
//
//        UserEntity user2 = new UserEntity();
//        user2.setEmail("_asddmdin2");
//        user2.setLogin("_ddmdin2");
//        user2.setPassword("_adddmin2");
//        user2 = userRepository.save(user2);
//
//        UserEntity user3 = new UserEntity();
//        user3.setEmail("_admdsdin3");
//        user3.setLogin("_adsddmin3");
//        user3.setPassword("_admdsidn3");
//        user3 = userRepository.save(user3);
//
//        UserEntity user4 = new UserEntity();
//        user4.setEmail("_admsddin4");
//        user4.setLogin("_addmin4");
//        user4.setPassword("_adddsmin4");
//        Set<UserEntity> friends = new HashSet<UserEntity>();
//        friends.add(user1);
//        friends.add(user2);
//        friends.add(user3);
//        user4.setFriends(friends);
//
//        user4 = userRepository.save(user4);
//
//        Node rootNode = neo4jTemplate.getNode(0);
//        Node node = neo4jTemplate.getNode(user4.getId());
//        rootNode.createRelationshipTo(node, new RelationshipType() {
//            @Override
//            public String name() {
//                return "EXIST";
//            }
//        });
//
//        return user4;

        UserEntity one1 = userRepository.findOne(4l);
        UserEntity one2 = userRepository.findOne(5l);
        UserEntity one3 = userRepository.findOne(6l);
        Set<UserEntity> friends = new HashSet<UserEntity>();
        friends.add(one1);
        friends.add(one2);
        one3.setFriends(friends);

        userRepository.save(one3);
        UserEntity one = userRepository.findOne(7l);
        return one3;

    }

    @RequestMapping(value = "createUser", method = RequestMethod.POST)
    @ResponseBody
    public User getUserByUsername(@RequestBody User user){
        return userService.createUser(user);
    }





}
