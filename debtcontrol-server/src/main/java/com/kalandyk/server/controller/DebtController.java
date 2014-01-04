package com.kalandyk.server.controller;

import com.kalandyk.api.model.Debt;

import com.kalandyk.server.neo4j.User;
import com.kalandyk.server.neo4j.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 1/4/14.
 */

@Controller
@RequestMapping("/debts")
@Transactional
public class DebtController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Debt> getDebtListForUser() {




        return null;
    }

}
