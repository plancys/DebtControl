package com.kalandyk.server.service;

import com.kalandyk.api.model.*;
import com.kalandyk.api.model.wrapers.Confirmations;
import com.kalandyk.api.model.wrapers.Debts;
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
            Confirmation confirmation = mapper.map(confirmationEntity, Confirmation.class);
            confirmations.getConfirmationList().add(confirmation);
        }
        return confirmations;
    }
}
