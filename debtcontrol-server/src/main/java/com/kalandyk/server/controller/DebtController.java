package com.kalandyk.server.controller;

import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.wrapers.Debts;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.server.neo4j.entity.DebtEntity;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.UserRepository;
import com.kalandyk.server.service.DebtService;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by kamil on 1/4/14.
 */

@Controller
@RequestMapping("/debts")
@Transactional
public class DebtController {

    @Autowired
    private DebtService debtService;
    @Autowired
    private Mapper mapper;

    @RequestMapping(value = "createDebt", method = RequestMethod.POST)
    @ResponseBody
    public Debt createDebt(@RequestBody Debt debt) throws DebtControlException {
        UserEntity creator = mapper.map(debt.getCreator(), UserEntity.class);
        DebtEntity savedDebt = debtService.createDebt(creator, mapToEntity(debt));
        return mapToDTO(savedDebt);
    }

    @RequestMapping(value = "getUserDebts", method = RequestMethod.POST)
    @ResponseBody
    public Debts getUserDebts(@RequestBody User user) throws DebtControlException {
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        Debts debts = new Debts();
        for(DebtEntity debtEntity : debtService.getUserDebts(userEntity)){
            debts.getDebts().add(mapToDTO(debtEntity));
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
