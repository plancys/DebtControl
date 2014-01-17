package com.kalandyk.server.service;

import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.ConfirmationType;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;
import com.kalandyk.server.neo4j.entity.ConfirmationEntity;
import com.kalandyk.server.neo4j.entity.DebtEntity;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.ConfirmationRepository;

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
    private Mapper mapper;

    public boolean createNewDebtConfirmation(User creator, Debt debt) {
        ConfirmationEntity confirmation = new ConfirmationEntity();
        confirmation.setConfirmationType(ConfirmationType.REQUEST_DEBT_ADDING);
        DebtEntity connectedDebt = mapper.map(debt, DebtEntity.class);
        confirmation.setConnectedDebt(connectedDebt);

        UserEntity creatorEntity = mapper.map(creator, UserEntity.class);
        confirmation.setRequestApplicant(creatorEntity);

        UserEntity receiver = getConfirmationReceiver(connectedDebt, creatorEntity);
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


}
