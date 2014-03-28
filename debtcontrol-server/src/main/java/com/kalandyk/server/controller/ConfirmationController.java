package com.kalandyk.server.controller;

import com.kalandyk.api.model.User;
import com.kalandyk.api.model.wrapers.ConfirmationDecision;
import com.kalandyk.api.model.wrapers.Confirmations;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.server.neo4j.entity.ConfirmationEntity;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.UserRepository;
import com.kalandyk.server.service.AuthenticationService;
import com.kalandyk.server.service.ConfirmationService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

/**
 * Created by kamil on 2/3/14.
 */

@Controller
@RequestMapping("/confirmations")
@Transactional
public class ConfirmationController {

    @Autowired
    private ConfirmationService confirmationService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Mapper mapper;

    @RequestMapping(value = "getUserConfirmations", method = RequestMethod.POST)
    @ResponseBody
    public Confirmations getConfirmations(@RequestBody User user) {
        //TODO: check whether authenticated user is the same with user from argument
        UserEntity userEntity = userRepository.findOne(user.getId());
        Set<ConfirmationEntity> confirmations = userEntity.getConfirmations();
        //TODO: convert confirmations to Confirmations object wrapper - confirmationsWrapper
        Confirmations confirmationsWrapper = new Confirmations();
        return confirmationsWrapper;
    }

    @RequestMapping(value = "sendConfirmationDecision", method = RequestMethod.POST)
    @ResponseBody
    public Boolean sendConfirmationDecision(@RequestBody ConfirmationDecision confirmationDecision) throws DebtControlException {
        ConfirmationEntity confirmation = mapper.map(confirmationDecision.getConfirmation(), ConfirmationEntity.class);
        //TODO: stupid type
        Boolean decision = confirmationService.sendDecision(confirmation, confirmationDecision.getDecision());
        return decision;
    }
}
