package com.kalandyk.server.service;

import com.kalandyk.api.model.*;
import com.kalandyk.api.model.wrapers.Debts;
import com.kalandyk.exception.IllegalDebtOperationException;
import com.kalandyk.server.neo4j.entity.*;
import com.kalandyk.server.neo4j.repository.*;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by kamil on 1/12/14.
 */

@Service
@Transactional
public class DebtService {

    @Autowired
    private Neo4jTemplate neo4jTemplate;

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
    private DebtHistoryRepository debtHistoryRepository;

    @Autowired
    private Mapper mapper;

    public Debt createDebt(User debtCreator, Debt debt) {
        if (debtExist(debt)) {
            return debt;
        }
        if (!userExist(debtCreator)) {
            return null;
        }
        //Prepare
        DebtEntity entityToSave = prepareNewDebtEntity(debt);
        addRelationToDebtFromUsers(entityToSave);
        confirmationService.createNewDebtConfirmation(debtCreator, mapper.map(entityToSave, Debt.class));
        entityToSave = debtRepository.save(prepareEventForDebt(entityToSave, DebtEventType.DEBT_ADDITION_REQUEST));
        return mapper.map(entityToSave, Debt.class);
    }

    private DebtEntity prepareEventForDebt(DebtEntity entityToSave, DebtEventType debtEventType) {
        DebtHistoryEntity historyEntity = entityToSave.getHistory();
        if(historyEntity == null){
            historyEntity = new DebtHistoryEntity();
            historyEntity = debtHistoryRepository.save(historyEntity);
        }
        DebtEventEntity addingEvent = prepareEvent(debtEventType);
        historyEntity.addEvent(addingEvent);
        historyEntity = debtHistoryRepository.save(historyEntity);
        entityToSave.setHistory(historyEntity);
        return entityToSave;
    }

    private DebtEventEntity prepareEvent(DebtEventType eventType) {
        DebtEventEntity debtEvent = new DebtEventEntity();
        debtEvent.setTime(new Date());
        debtEvent.setEventType(eventType);
        DebtEventEntity savedEvent = debtEventRepository.save(debtEvent);
        return savedEvent;
    }

    private DebtEntity prepareNewDebtEntity(Debt debt) {
        DebtEntity entityToSave = mapper.map(debt, DebtEntity.class);
        entityToSave.updateTimestamp();
        entityToSave.setDebtState(DebtState.NOT_CONFIRMED_DEBT);
        //Save
        entityToSave = debtRepository.save(entityToSave);
        return entityToSave;
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
        if (debtCreator.getId() == null) {
            return false;
        }
        UserEntity user = userRepository.findOne(debtCreator.getId());
        return user != null;
    }

    public boolean makeDecisionRegardingRepayDebtRequest(Debt debt, boolean decision) throws IllegalDebtOperationException {
        DebtEntity connectedDebt = mapper.map(debt, DebtEntity.class);
        connectedDebt = debtRepository.save(connectedDebt);

        if (decision) {
            connectedDebt.setDebtState(DebtState.CONFIRMED_REPAID_DEBT);
            connectedDebt = prepareEventForDebt(connectedDebt, DebtEventType.DEBT_APPROVING_REPAYMENT_REQUEST);

        } else {
            connectedDebt.setDebtState(DebtState.CONFIRMED_NOT_REPAID_DEBT);
            connectedDebt = prepareEventForDebt(connectedDebt, DebtEventType.DEBT_REJECTING_REPAYMENT_REQUEST);
        }
        debtRepository.save(connectedDebt);
        return true;
    }

    public boolean debtExist(Debt debt) {
        if (debt.getId() == null) {
            return false;
        }
        return debtRepository.findOne(debt.getId()) != null;
    }


    public Debts getUserDebts(UserCredentials credentials) {
        UserEntity user = userRepository.findByLogin(credentials.getLogin());
        if (user == null) {//|| !user.getPassword().equals(credentials.getPassword())){
            //TODO: check users credentials
            return null;
        }

        Debts debts = new Debts();
        for (DebtEntity debt : user.getWeOwesSbDebts()) {
            debt = debtRepository.findOne(debt.getId());
            Debt debtToAdd = mapper.map(debt, Debt.class);
            debtToAdd.setDebtPosition(DebtPosition.DEBTOR);
            debts.getDebts().add(debtToAdd);
        }

        for (DebtEntity debt : user.getSbOwesToUsDebts()) {
            debt = debtRepository.findOne(debt.getId());
            Debt debtToAdd = mapper.map(debt, Debt.class);
            debtToAdd.setDebtPosition(DebtPosition.CREDITOR);
            debts.getDebts().add(debtToAdd);
        }


        return debts;
    }

    public void makeDecisionRegardingAddingDebtRequest(Debt debt, boolean decision) {
        DebtEntity connectedDebt = mapper.map(debt, DebtEntity.class);
        connectedDebt = debtRepository.save(connectedDebt);

        if (decision) {
            connectedDebt.setDebtState(DebtState.CONFIRMED_NOT_REPAID_DEBT);
            connectedDebt = prepareEventForDebt(connectedDebt, DebtEventType.DEBT_APPROVING_REPAYMENT_REQUEST);
        } else {
            connectedDebt.setDebtState(DebtState.DELETED);
            connectedDebt = prepareEventForDebt(connectedDebt, DebtEventType.DEBT_APPROVING_REPAYMENT_REQUEST);
        }

        debtRepository.save(connectedDebt);
    }

    public Debt requestDebtRepaying(Debt debt) {
        if (debt == null) {
            return null;
        }
        DebtEntity debtEntity = mapper.map(debt, DebtEntity.class);
        debtEntity = debtRepository.findOne(debtEntity.getId());

        if (!debtEntity.getDebtState().equals(DebtState.CONFIRMED_NOT_REPAID_DEBT)) {
            return null;
        }

        debtEntity.setDebtState(DebtState.CONFIRMED_DEBT_WITH_NO_CONFIRMED_REPAYMENT);
        debtEntity = debtRepository.save(debtEntity);

        debt = mapper.map(debtEntity, Debt.class);
        confirmationService.createDebtRepayingConfirmation(debt);

        return debt;
    }

    public Debt cancelDebtRepayingRequest(Debt debt) {
        if (!debt.getDebtState().equals(DebtState.CONFIRMED_DEBT_WITH_NO_CONFIRMED_REPAYMENT)) {
            //TODO: raise some exception
        }
        debt.setDebtState(DebtState.CONFIRMED_NOT_REPAID_DEBT);
        DebtEntity savedDebt = mapper.map(debt, DebtEntity.class);
        savedDebt = debtRepository.save(savedDebt);
        removeConnectedConfirmation(savedDebt);
        return mapper.map(savedDebt, Debt.class);
    }

    private void removeConnectedConfirmation(DebtEntity savedDebt) {
        UserEntity creditor = userRepository.findOne(savedDebt.getCreditor().getId());
        ConfirmationEntity confirmationToRemove = null;
        for (ConfirmationEntity confirmation : creditor.getConfirmations()) {
            confirmation = confirmationRepository.findOne(confirmation.getId());
            if (confirmation.getConnectedDebt().equals(savedDebt)) {
                confirmationToRemove = confirmation;
                break;
            }
        }
        if (confirmationToRemove != null) {
            confirmationRepository.delete(confirmationToRemove);
        }
    }
}
