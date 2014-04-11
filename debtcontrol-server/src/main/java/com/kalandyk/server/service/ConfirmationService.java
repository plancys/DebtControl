package com.kalandyk.server.service;

import com.kalandyk.api.model.ConfirmationType;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.exception.ExceptionType;
import com.kalandyk.server.neo4j.entity.DebtConfirmationEntity;
import com.kalandyk.server.neo4j.entity.DebtEntity;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.ConfirmationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConfirmationService {

    @Autowired
    private ConfirmationRepository confirmationRepository;
    @Autowired
    private DebtService debtService;

    public void createNewDebtConfirmation(UserEntity creator, DebtEntity debt) throws DebtControlException {
        DebtConfirmationEntity confirmation = new DebtConfirmationEntity();
        confirmation.setConfirmationType(ConfirmationType.REQUEST_DEBT_ADDING);
        confirmation.setConnectedDebt(debt);
        creator.updateTimestamp();
        confirmation.setRequestApplicant(creator);
        UserEntity receiver = getConfirmationReceiver(debt, creator);
        receiver.updateTimestamp();
        confirmation.setReceiver(receiver);

        saveConfirmation(confirmation);
    }

    public DebtEntity sendDecision(DebtConfirmationEntity confirmation, boolean decision) throws DebtControlException {
        ConfirmationType confirmationType = confirmation.getConfirmationType();
        DebtEntity debtAfterDecision = null;
        //TODO: add abstract builder or sth
        switch (confirmationType) {
            case REQUEST_DEBT_ADDING:
                debtAfterDecision = debtService.makeDecisionRegardingAddingDebtRequest(confirmation.getConnectedDebt(), decision);
                break;
            case REQUEST_DEBT_REPAYING:
                debtAfterDecision = debtService.makeDecisionRegardingRepayDebtRequest(confirmation.getConnectedDebt(), decision);
                break;
        }
        try {
            confirmationRepository.delete(confirmation);
        } catch (Exception e) {
            throw new DebtControlException(ExceptionType.CONFIRMATION_CREATION_EXCEPTION,
                    new StringBuilder()
                            .append("Error with deleting confirmation: ")
                            .append(e.getMessage()).toString());
        }
        return debtAfterDecision;
    }

    public void createDebtRepayingConfirmation(DebtEntity debt) throws DebtControlException {
        DebtConfirmationEntity confirmation = new DebtConfirmationEntity();
        confirmation.setConnectedDebt(debt);
        confirmation.setConfirmationType(ConfirmationType.REQUEST_DEBT_REPAYING);
        confirmation.setReceiver(debt.getCreditor());
        confirmation.setRequestApplicant(debt.getDebtor());
        saveConfirmation(confirmation);
    }

//    public void createFriendInvitationConfirmation(UserEntity requester, UserEntity target) throws DebtControlException {
//        DebtConfirmationEntity confirmationEntity = new DebtConfirmationEntity();
//        confirmationEntity.setReceiver(target);
//        confirmationEntity.setRequestApplicant(requester);
//        confirmationEntity.setConfirmationType(ConfirmationType.REQUEST_CREATE_FRIENDSHIP);
//        saveConfirmation(confirmationEntity);
//
//    }

    private void saveConfirmation(DebtConfirmationEntity confirmation) throws DebtControlException {
        try {
            confirmationRepository.save(confirmation);
        } catch (Exception e) {
            throw new DebtControlException(ExceptionType.CONFIRMATION_CREATION_EXCEPTION,
                    new StringBuilder()
                            .append("Error with creation of confirmation.")
                            .append("Details:").append(e.getMessage()).toString()
            );
        }
    }

    private UserEntity getConfirmationReceiver(DebtEntity connectedDebt, UserEntity creatorEntity) {
        if (creatorEntity.equals(connectedDebt.getDebtor())) {
            return connectedDebt.getCreditor();
        }
        return connectedDebt.getDebtor();
    }
}
