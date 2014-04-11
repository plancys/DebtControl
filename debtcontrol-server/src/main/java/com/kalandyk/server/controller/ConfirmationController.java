package com.kalandyk.server.controller;

import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.wrapers.ConfirmationDecision;
import com.kalandyk.api.model.wrapers.Confirmations;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.server.neo4j.entity.DebtConfirmationEntity;
import com.kalandyk.server.neo4j.entity.DebtEntity;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.ConfirmationRepository;
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

@Controller
@RequestMapping("secured/confirmations")
@Transactional
public class ConfirmationController {

    @Autowired
    private ConfirmationService confirmationService;
    @Autowired
    private ConfirmationRepository confirmationRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "getUserConfirmations", method = RequestMethod.GET)
    @ResponseBody
    public Confirmations getConfirmations() {
        UserEntity authenticatedUser = authenticationService.getAuthenticatedUser();
        Set<DebtConfirmationEntity> confirmations = authenticatedUser.getConfirmations();
        Confirmations confirmationsWrapper = new Confirmations();
        for (DebtConfirmationEntity confirmationEntity : confirmations) {
            confirmationEntity = fetchConfirmation(confirmationEntity);
            confirmationsWrapper.getConfirmationList().add(mapToDTO(confirmationEntity));
        }
        return confirmationsWrapper;
    }

    @RequestMapping(value = "sendConfirmationDecision", method = RequestMethod.POST)
    @ResponseBody
    public Debt sendConfirmationDecision(@RequestBody ConfirmationDecision confirmationDecision) throws DebtControlException {
        DebtConfirmationEntity confirmation = mapper.map(confirmationDecision.getConfirmation(), DebtConfirmationEntity.class);
        DebtEntity debtEntity = confirmationService.sendDecision(confirmation, confirmationDecision.getDecision());
        return mapper.map(debtEntity, Debt.class);
    }

    private Confirmation mapToDTO(DebtConfirmationEntity confirmationEntity) {
        return mapper.map(confirmationEntity, Confirmation.class);
    }

    private DebtConfirmationEntity fetchConfirmation(DebtConfirmationEntity confirmationEntity) {
        confirmationEntity = confirmationRepository.findOne(confirmationEntity.getId());
        return confirmationEntity;
    }
}
