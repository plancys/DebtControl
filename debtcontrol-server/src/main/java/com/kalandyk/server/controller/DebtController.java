package com.kalandyk.server.controller;

import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtPosition;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.wrapers.Debts;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.server.neo4j.entity.DebtEntity;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.DebtRepository;
import com.kalandyk.server.neo4j.repository.UserRepository;
import com.kalandyk.server.service.DebtService;
import com.kalandyk.server.utils.AuthUtil;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kamil on 1/4/14.
 */

@Controller
@RequestMapping("secured/debts")
@Transactional
public class DebtController {

    @Autowired
    private DebtService debtService;
    @Autowired
    private DebtRepository debtRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Mapper mapper;

    @RequestMapping(value = "createDebt", method = RequestMethod.POST)
    @ResponseBody
    public Debt createDebt(@RequestBody Debt debt) throws DebtControlException {
        DebtEntity savedDebt = debtService
                .createDebt(AuthUtil.getAuthenticatedUser(userRepository), mapToEntity(debt));
        return mapToDTO(savedDebt);
    }

    //TODO: move to service
    @RequestMapping(value = "getUserDebts", method = RequestMethod.GET)
    @ResponseBody
    public Debts getUserDebts() throws DebtControlException {
        Debts debts = new Debts();
        UserEntity authenticatedUser = AuthUtil.getAuthenticatedUser(userRepository);
        for (DebtEntity debtEntity : debtService.getUserDebts(authenticatedUser)) {
            debtEntity = debtRepository.findOne(debtEntity.getId());
            Debt debt = mapToDTO(debtEntity);
            if (debt.getCreditor().equals(mapper.map(authenticatedUser, User.class))) {
                debt.setDebtPosition(DebtPosition.CREDITOR);
            } else {
                debt.setDebtPosition(DebtPosition.DEBTOR);
            }
            debts.getDebts().add(debt);
        }
        return debts;
    }

    @RequestMapping(value = "requestDebtRepaying", method = RequestMethod.POST)
    @ResponseBody
    public Debt requestDebtRepaying(@RequestBody Debt debt) throws DebtControlException {
        DebtEntity debtEntity = debtService.requestDebtRepaying(mapToEntity(debt));
        return mapToDTO(debtEntity);
    }

    @RequestMapping(value = "cancelDebtRepayingRequest", method = RequestMethod.POST)
    @ResponseBody
    public Debt cancelDebtRepayingRequest(@RequestBody Debt debt) throws DebtControlException {
        DebtEntity debtEntity = debtService.cancelDebtRepayingRequest(mapToEntity(debt));
        return mapToDTO(debtEntity);
    }

    private Debt mapToDTO(DebtEntity debtEntity) {
        return mapper.map(debtEntity, Debt.class);
    }

    private DebtEntity mapToEntity(Debt debt) {
        return mapper.map(debt, DebtEntity.class);
    }
}
