package com.kalandyk.server.controller;

import com.kalandyk.api.model.User;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.server.neo4j.entity.UserEntity;
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
@RequestMapping("unsecured")
@Transactional
public class RegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private Mapper mapper;

    @RequestMapping(value = "createUser", method = RequestMethod.POST)
    @ResponseBody
    public User createUser(@RequestBody User user) throws DebtControlException {
        UserEntity userEntity = mapToEntity(user);
        userEntity = userService.createUser(userEntity);
        return mapToDTO(userEntity);
    }

    private UserEntity mapToEntity(User user) {
        return mapper.map(user, UserEntity.class);
    }

    private User mapToDTO(UserEntity approver) {
        return mapper.map(approver, User.class);
    }
}
