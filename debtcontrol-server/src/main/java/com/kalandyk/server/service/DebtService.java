package com.kalandyk.server.service;

import com.kalandyk.api.model.*;
import com.kalandyk.api.model.wrapers.Debts;
import com.kalandyk.exception.IllegalDebtOperationException;
import com.kalandyk.server.neo4j.entity.ConfirmationEntity;
import com.kalandyk.server.neo4j.entity.DebtEntity;
import com.kalandyk.server.neo4j.entity.DebtHistoryEntity;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.ConfirmationRepository;
import com.kalandyk.server.neo4j.repository.DebtRepository;
import com.kalandyk.server.neo4j.repository.DebtEventRepository;
import com.kalandyk.server.neo4j.repository.UserRepository;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by kamil on 1/12/14.
 */

@Service
@Transactional
public class DebtService {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @Autowired
    private ConfirmationService confirmationService;

    @Autowired
    private DebtEventRepository debtEventRepository;

    @Autowired
    private  DebtEventService debtEventService;

    @Autowired
    private Mapper mapper;

    public Debt createDebt(User debtCreator, Debt debt) {
        if (debtExist(debt)) {
            return debt;
        }
        if( !userExist(debtCreator) ){
            return null;
        }
        //Prepare
        DebtEntity entityToSave = mapper.map(debt, DebtEntity.class);
        entityToSave.updateTimestamp();
        entityToSave.setDebtState(DebtState.NOT_CONFIRMED_DEBT);
        //Save
        entityToSave = debtRepository.save(entityToSave);

        addRelationToDebtFromUsers(entityToSave);


        boolean confirmationCreated = confirmationService.createNewDebtConfirmation(debtCreator, mapper.map(entityToSave, Debt.class));

        if( !confirmationCreated ){
            //TODO: raise some exception or sth
        }

        addEvents(debtCreator, debt, entityToSave);

        return mapper.map(entityToSave, Debt.class);
    }

    private void addEvents(User debtCreator, Debt debt, DebtEntity entityToSave) {
        if(true){
            //TODO: temporary disabled
            return;
        }
        List<DebtEvent> event = debtEventService.createEvent(debtCreator, debt, DebtEventType.DEBT_ADDITION_REQUEST);
        DebtHistoryEntity history = entityToSave.getHistory();
    }

    private void addRelationToDebtFromUsers(DebtEntity entityToSave) {
        UserEntity creditor = entityToSave.getCreditor();
        creditor = userRepository.findOne(creditor.getId());
        creditor.getSbOwesToUsDebts().add(entityToSave);

        UserEntity debtor = entityToSave.getDebtor();
        debtor = userRepository.findOne(debtor.getId());
        debtor.getWeOwesSbDebts().add(entityToSave);

        debtor = userRepository.save(debtor);
        creditor = userRepository.save(creditor);
    }


    private boolean userExist(User debtCreator) {
        if(debtCreator.getId() == null){
            return false;
        }
        UserEntity user = userRepository.findOne(debtCreator.getId());
        return user != null;
    }

    public boolean requestRepayDebt(User user, Debt debt) throws IllegalDebtOperationException {
        UserEntity requester = mapper.map(user, UserEntity.class);
        DebtEntity connectedDebt = mapper.map(debt, DebtEntity.class);

//        if (!connectedDebt.getDebtor().equals(requester)) {
//            throw new IllegalDebtOperationException();
//        }
//        if (!connectedDebt.getDebtWithConfirmationState().equals(DebtWithConfirmationState.CONFIRMED_NOT_REPAID_DEBT)) {
//            throw new IllegalDebtOperationException();
//        }
        connectedDebt.setDebtState(DebtState.CONFIRMED_DEBT_WITH_NO_CONFIRMED_REPAYMENT);
        debtRepository.save(connectedDebt);

        ConfirmationEntity confirmation = getConfirmationForDebtRepayingRequest(requester, connectedDebt);
        confirmationRepository.save(confirmation);

        return true;

    }

    public boolean acceptRepayDebtRequest(User user, Debt debt) throws IllegalDebtOperationException {
        UserEntity approver = mapper.map(user, UserEntity.class);
        DebtEntity connectedDebt = mapper.map(debt, DebtEntity.class);

//        if (!connectedDebt.getDebtor().equals(approver)) {
//            throw new IllegalDebtOperationException();
//        }
//        if (!connectedDebt.getDebtWithConfirmationState().equals(DebtWithConfirmationState.CONFIRMED_DEBT_WITH_NO_CONFIRMED_REPAYMENT)) {
//            throw new IllegalDebtOperationException();
//        }

        connectedDebt.setDebtState(DebtState.CONFIRMED_REPAID_DEBT);
        debtRepository.save(connectedDebt);

        return true;
    }

    private ConfirmationEntity getConfirmationForDebtRepayingRequest(UserEntity requester, DebtEntity connectedDebt) {
        ConfirmationEntity confirmation = new ConfirmationEntity();
        confirmation.setConfirmationType(ConfirmationType.REQUEST_DEBT_REPAYING);
        confirmation.setConnectedDebt(connectedDebt);
        confirmation.setRequestApplicant(requester);
        return confirmation;
    }

    public boolean debtExist(Debt debt) {
        if (debt.getId() == null) {
            return false;
        }
        return debtRepository.findOne(debt.getId()) != null;
    }


    public Debts getUserDebts(UserCredentials credentials) {
        UserEntity user = userRepository.findByLogin(credentials.getLogin());
        if(user == null ) {//|| !user.getPassword().equals(credentials.getPassword())){
            //TODO: check users credentials
            return null;
        }

        Debts debts = new Debts();
        for(DebtEntity debt : user.getWeOwesSbDebts()){
            debt = debtRepository.findOne(debt.getId());
            Debt debtToAdd = mapper.map(debt, Debt.class);
            debtToAdd.setDebtPosition(DebtPosition.DEBTOR);
            debts.getDebts().add(debtToAdd);
        }

        for(DebtEntity debt : user.getSbOwesToUsDebts()){
            debt = debtRepository.findOne(debt.getId());
            Debt debtToAdd = mapper.map(debt, Debt.class);
            debtToAdd.setDebtPosition(DebtPosition.CREDITOR);
            debts.getDebts().add(debtToAdd);
        }


        return debts;
    }

    public void acceptAddingDebtRequest(User receiver, Debt connectedDebt) {
        connectedDebt.setDebtState(DebtState.CONFIRMED_NOT_REPAID_DEBT);
        debtRepository.save(mapper.map(connectedDebt, DebtEntity.class));
    }
}
