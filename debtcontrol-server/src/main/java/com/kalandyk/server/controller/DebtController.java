package com.kalandyk.server.controller;

import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;
import com.kalandyk.api.model.wrapers.Debts;
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

    @RequestMapping(value = "createDebt", method = RequestMethod.POST)
    @ResponseBody
    public Debt createDebt(@RequestBody Debt debt) {
        return debtService.createDebt(debt.getCreator(), debt);
    }

    @RequestMapping(value = "getUserDebts", method = RequestMethod.POST)
     @ResponseBody
     public Debts getUserDebts(@RequestBody UserCredentials credentials) {
        return debtService.getUserDebts(credentials);
    }

    @RequestMapping(value = "requestDebtRepaying", method = RequestMethod.POST)
    @ResponseBody
    public Debt requestDebtRepaying(@RequestBody Debt debt) {
        return debtService.requestDebtRepaying(debt);
    }

    @RequestMapping(value = "cancelDebtRepayingRequest", method = RequestMethod.POST)
    @ResponseBody
    public Debt cancelDebtRepayingRequest(@RequestBody Debt debt) {
        return debtService.cancelDebtRepayingRequest(debt);
    }
}
