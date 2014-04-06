package com.kalandyk.server.service;

import com.kalandyk.api.model.DebtEventType;
import com.kalandyk.api.model.DebtState;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.exception.ExceptionType;
import com.kalandyk.server.neo4j.entity.*;
import com.kalandyk.server.neo4j.repository.*;
import com.kalandyk.server.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DebtService extends AbstractDebtService {

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

    public DebtEntity createDebt(UserEntity debtCreator, DebtEntity debt) throws DebtControlException {
        debtCreator = fetchUser(debtCreator);
        //TODO: handle situation when debtCreator doesn't have debtor/creditor in friends
        if (debtExist(debt)) {
            return debt;
        }
        if (debtCreator == null) {
            throw new DebtControlException(ExceptionType.DEBT_CREATION_EXCEPTION,
                    "Creator doesn't exist");
        }
        if (debt.getCreditor() == null || debt.getDebtor() == null) {
            throw new DebtControlException(ExceptionType.DEBT_CREATION_EXCEPTION,
                    "Lack of debtor/creditor in creating debt");
        }
        debt.updateTimestamp();
        debt.setDebtState(DebtState.NOT_CONFIRMED_DEBT);
        addRelationToDebtFromUsers(debt);
        confirmationService.createNewDebtConfirmation(debtCreator, debt);
        prepareEventForDebt(debt, DebtEventType.DEBT_ADDITION_REQUEST);
        return debtRepository.save(debt);
    }

    public DebtEntity makeDecisionRegardingRepayDebtRequest(DebtEntity debt, boolean decision) throws DebtControlException {
        debt = refreshDebtAndCheckIfExist(debt);
        if (decision) {
            debt.setDebtState(DebtState.CONFIRMED_REPAID_DEBT);
            prepareEventForDebt(debt, DebtEventType.DEBT_APPROVING_REPAYMENT_REQUEST);

        } else {
            debt.setDebtState(DebtState.CONFIRMED_NOT_REPAID_DEBT);
            prepareEventForDebt(debt, DebtEventType.DEBT_REJECTING_REPAYMENT_REQUEST);
        }
        return saveDebt(debt);
    }

    public List<DebtEntity> getUserDebts(UserEntity user) throws DebtControlException {
        user = fetchUser(user);
        List<DebtEntity> debtEntities = new ArrayList<DebtEntity>();
        for (DebtEntity debt : user.getWeOwesSbDebts()) {
            refreshDebtAndCheckIfExist(debt);
            debtEntities.add(debt);
        }

        for (DebtEntity debt : user.getSbOwesToUsDebts()) {
            refreshDebtAndCheckIfExist(debt);
            debtEntities.add(debt);
        }
        return debtEntities;
    }

    public DebtEntity makeDecisionRegardingAddingDebtRequest(DebtEntity debt, boolean decision) throws DebtControlException {
        debt = refreshDebtAndCheckIfExist(debt);
        if (!debt.getDebtState().equals(DebtState.NOT_CONFIRMED_DEBT)) {
            throw new DebtControlException(ExceptionType.DEBT_EDITION_EXCEPTION,
                    new StringBuilder()
                            .append("Incorrect debt state changing operation.")
                            .append("You can request adding debt only on NOT_CONFIRMED_DEBT state not on ")
                            .append(debt.getDebtState()).toString());
        }
        if (decision) {
            debt.setDebtState(DebtState.CONFIRMED_NOT_REPAID_DEBT);
            prepareEventForDebt(debt, DebtEventType.DEBT_ADDITION_APPROVING);
        } else {
            debt.setDebtState(DebtState.DELETED);
            prepareEventForDebt(debt, DebtEventType.DEBT_APPROVING_REPAYMENT_REQUEST);
        }
        return saveDebt(debt);
    }

    public DebtEntity requestDebtRepaying(DebtEntity debt) throws DebtControlException {
        debt = refreshDebtAndCheckIfExist(debt);
        if (!debt.getDebtState().equals(DebtState.CONFIRMED_NOT_REPAID_DEBT)) {
            throw new DebtControlException(ExceptionType.DEBT_EDITION_EXCEPTION,
                    new StringBuilder()
                            .append("Incorrect debt state changing operation.")
                            .append("You can request debt repaying only on CONFIRMED_NOT_REPAID_DEBT state not on ")
                            .append(debt.getDebtState()).toString());

        }
        checkPermisionForDebtRepaying(debt);

        debt.setDebtState(DebtState.CONFIRMED_DEBT_WITH_PENDING_REPAYMENT_APPROVAL);
        prepareEventForDebt(debt, DebtEventType.DEBT_REPAYMENT_REQUEST);
        confirmationService.createDebtRepayingConfirmation(debt);
        return saveDebt(debt);
    }

    public DebtEntity cancelDebtRepayingRequest(DebtEntity debt) throws DebtControlException {
        debt = refreshDebtAndCheckIfExist(debt);
        if (!debt.getDebtState().equals(DebtState.CONFIRMED_DEBT_WITH_PENDING_REPAYMENT_APPROVAL)) {
            throw new DebtControlException(ExceptionType.DEBT_EDITION_EXCEPTION,
                    new StringBuilder()
                            .append("Incorrect debt state changing operation.")
                            .append("You can request cancel debt repaying ")
                            .append("only on CONFIRMED_DEBT_WITH_PENDING_REPAYMENT_APPROVAL state ")
                            .append("not on ").append(debt.getDebtState()).toString());
        }
        checkPermisionForDebtRepaying(debt);
        debt.setDebtState(DebtState.CONFIRMED_NOT_REPAID_DEBT);
        prepareEventForDebt(debt, DebtEventType.DEBT_CANCELING_REPAYMENT_REQUEST);
        debt = debtRepository.save(debt);
        removeConnectedConfirmation(debt);
        return debt;
    }

    private void checkPermisionForDebtRepaying(DebtEntity debt) throws DebtControlException {
        if (!debt.getDebtor().equals(AuthUtil.getAuthenticatedUser(userRepository))) {
            throw new DebtControlException(ExceptionType.PERMISSION_DENIED,
                    new StringBuilder()
                            .append("Only debtor can request this change.").toString());

        }
    }

    private DebtEntity saveDebt(DebtEntity debt) throws DebtControlException {
        try {
            return debtRepository.save(debt);
        } catch (Exception e) {
            throw new DebtControlException(ExceptionType.DEBT_EDITION_EXCEPTION, e.getMessage());
        }
    }

    private DebtEntity refreshDebtAndCheckIfExist(DebtEntity debt) throws DebtControlException {
        debt = debtRepository.findOne(debt.getId());
        if (debt == null) {
            throw new DebtControlException(ExceptionType.DEBT_EDITION_EXCEPTION, "Editing not existing debt");
        }
        return debt;
    }

    private boolean debtExist(DebtEntity debt) {
        if (debt.getId() == null) {
            return false;
        }
        return debtRepository.findOne(debt.getId()) != null;
    }

    private void prepareEventForDebt(DebtEntity debt, DebtEventType debtEventType) {
        DebtHistoryEntity historyEntity = debt.getHistory();
        if (historyEntity == null) {
            historyEntity = new DebtHistoryEntity();
            historyEntity = debtHistoryRepository.save(historyEntity);
        }
        DebtEventEntity addingEvent = prepareEvent(debtEventType);
        historyEntity.addEvent(addingEvent);
        historyEntity = debtHistoryRepository.save(historyEntity);
        debt.setHistory(historyEntity);
    }

    private DebtEventEntity prepareEvent(DebtEventType eventType) {
        DebtEventEntity debtEvent = new DebtEventEntity();
        debtEvent.setTime(new Date());
        debtEvent.setEventType(eventType);
        return debtEventRepository.save(debtEvent);
    }

    private void addRelationToDebtFromUsers(DebtEntity debt) throws DebtControlException {
        UserEntity creditor = fetchUser(debt.getCreditor());
        creditor.getSbOwesToUsDebts().add(debt);
        UserEntity debtor = fetchUser(debt.getDebtor());
        debtor.getWeOwesSbDebts().add(debt);

        try {
            userRepository.save(debtor);
            userRepository.save(creditor);
        } catch (Exception e) {
            throw new DebtControlException(ExceptionType.CONFIRMATION_CREATION_EXCEPTION,
                    new StringBuilder()
                            .append("Error with adding relation to users from debt.")
                            .append("Details:").append(e.getMessage()).toString()
            );
        }
    }

    private UserEntity fetchUser(UserEntity creditor) {
        return userRepository.findOne(creditor.getId());
    }

    private void removeConnectedConfirmation(DebtEntity savedDebt) {
        UserEntity creditor = fetchUser(savedDebt.getCreditor());
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
