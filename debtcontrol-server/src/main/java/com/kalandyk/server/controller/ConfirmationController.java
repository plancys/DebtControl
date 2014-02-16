package com.kalandyk.server.controller;

import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.UserCredentials;
import com.kalandyk.api.model.wrapers.ConfirmationDecision;
import com.kalandyk.api.model.wrapers.Confirmations;
import com.kalandyk.api.model.wrapers.Debts;
import com.kalandyk.exception.IllegalDebtOperationException;
import com.kalandyk.server.service.ConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kamil on 2/3/14.
 */

@Controller
@RequestMapping("/confirmations")
@Transactional
public class ConfirmationController {

    @Autowired
    private ConfirmationService confirmationService;

    @RequestMapping(value = "getUserConfirmations", method = RequestMethod.POST)
    @ResponseBody
    public Confirmations getConfirmations(@RequestBody UserCredentials credentials) {
        return confirmationService.getUserConfirmations(credentials);
    }

    @RequestMapping(value = "sendConfirmationDecision", method = RequestMethod.POST)
    @ResponseBody
    public Boolean sendConfirmationDecision(@RequestBody ConfirmationDecision confirmationDecision) throws IllegalDebtOperationException {
        return confirmationService.sendDecision(confirmationDecision.getConfirmation(), confirmationDecision.getDecision());
    }
}
