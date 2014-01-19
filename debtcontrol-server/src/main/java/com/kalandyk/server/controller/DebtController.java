package com.kalandyk.server.controller;

import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;
import com.kalandyk.server.service.DebtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by kamil on 1/4/14.
 */

@Controller
@RequestMapping("/debts")
@Transactional
public class DebtController {


    @Autowired
    private DebtService debtService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public Debt saveTest() {
        Debt debt = new Debt();
        debt.setCreationDate(new Date());
        User creator = new User();
        creator.setId(3l);
        //debt.setDebtor(creator);
        debt.setCreator(creator);


        debt.setAmount(100l);
        User debtor = new User();
        debtor.setId(3l);
        debt.setDebtor(debtor);
        User creditor = new User();
        creditor.setId(4l);
        debt.setDescription("Flaszka wodki");
        debt.setCreditor(creditor);
        return debtService.createDebt(debt.getCreator(), debt);
    }

    @RequestMapping(value = "createDebt", method = RequestMethod.POST)
    @ResponseBody
    public Debt createUser(@RequestBody Debt debt) {
        return debtService.createDebt(debt.getCreator(), debt);
    }

}
