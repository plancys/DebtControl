package com.kalandyk.server.service;

import com.kalandyk.api.model.*;
import com.kalandyk.api.model.wrapers.Confirmations;
import com.kalandyk.exception.IllegalDebtOperationException;
import com.kalandyk.server.neo4j.entity.ConfirmationEntity;
import com.kalandyk.server.neo4j.entity.DebtEntity;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.ConfirmationRepository;

import com.kalandyk.server.neo4j.repository.UserRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kamil on 1/14/14.
 */
@Service
public class ConfirmationService {

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DebtService debtService;

    @Autowired
    private Mapper mapper;

    public boolean createNewDebtConfirmation(User creator, Debt debt) {
        ConfirmationEntity confirmation = new ConfirmationEntity();
        confirmation.setConfirmationType(ConfirmationType.REQUEST_DEBT_ADDING);
        DebtEntity connectedDebt = mapper.map(debt, DebtEntity.class);
        connectedDebt.updateTimestamp();
        confirmation.setConnectedDebt(connectedDebt);

        UserEntity creatorEntity = mapper.map(creator, UserEntity.class);
        creatorEntity.updateTimestamp();
        confirmation.setRequestApplicant(creatorEntity);

        UserEntity receiver = getConfirmationReceiver(connectedDebt, creatorEntity);
        receiver.updateTimestamp();
        confirmation.setReceiver(receiver);

        confirmation = confirmationRepository.save(confirmation);

        return confirmation != null;
    }

    public Boolean sendDecision(Confirmation confirmation, boolean decision) throws IllegalDebtOperationException {
        ConfirmationType confirmationType = confirmation.getConfirmationType();
        //TODO: add abstract builder or sth
        switch (confirmationType){
            case REQUEST_DEBT_ADDING:
                debtService.acceptAddingDebtRequest(confirmation.getReceiver(), confirmation.getConnectedDebt());
                confirmationRepository.delete(mapper.map(confirmation, ConfirmationEntity.class));
                break;
            case REQUEST_DEBT_REPAYING:
                debtService.acceptRepayDebtRequest(confirmation.getReceiver(), confirmation.getConnectedDebt());
                confirmationRepository.delete(mapper.map(confirmation, ConfirmationEntity.class));
                break;
        }

        return true;
    }

    public boolean acceptConfirmationAnswer(Confirmation confirmation){
        return false;
    }

    public boolean rejectConfirmationAnswer(Confirmation confirmation){
        return false;
    }


    private UserEntity getConfirmationReceiver(DebtEntity connectedDebt, UserEntity creatorEntity) {
        if (creatorEntity.equals(connectedDebt.getDebtor())) {
            return connectedDebt.getCreditor();
        }
        return connectedDebt.getDebtor();
    }

    public Confirmations getUserConfirmations(UserCredentials credentials) {
        UserEntity user = userRepository.findByLogin(credentials.getLogin());
        if(user == null ) {//|| !user.getPassword().equals(credentials.getPassword())){
            //TODO: check users credentials
            return null;
        }

        Confirmations confirmations = new Confirmations();
        for(ConfirmationEntity confirmationEntity : user.getConfirmations()){
            confirmationEntity = confirmationRepository.findOne(confirmationEntity.getId());
            eraseFriendsFromEntity(confirmationEntity);
            Confirmation confirmation = mapper.map(confirmationEntity, Confirmation.class);
            confirmations.getConfirmationList().add(confirmation);
        }
        return confirmations;
    }

    private void eraseFriendsFromEntity(ConfirmationEntity confirmationEntity) {
        //TODO: find a better way to do that
        if(confirmationEntity != null){
            confirmationEntity.getReceiver().setFriends(null);
            confirmationEntity.getRequestApplicant().setFriends(null);
        }
    }

    public void createDebtRepayingConfirmation(Debt debt) {
        Confirmation confirmation = new Confirmation();
        confirmation.setConnectedDebt(debt);
        confirmation.setConfirmationType(ConfirmationType.REQUEST_DEBT_REPAYING);
        confirmation.setReceiver(debt.getCreditor());
        confirmation.setRequestApplicant(debt.getDebtor());
        ConfirmationEntity savedConfirmation = confirmationRepository.save(mapper.map(confirmation, ConfirmationEntity.class));
    }
}
