package com.kalandyk.server.service;

import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtEvent;
import com.kalandyk.api.model.DebtEventType;
import com.kalandyk.api.model.User;
import com.kalandyk.server.neo4j.entity.DebtEntity;
import com.kalandyk.server.neo4j.entity.DebtEventEntity;
import com.kalandyk.server.neo4j.entity.DebtHistoryEntity;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.DebtEventRepository;
import com.kalandyk.server.neo4j.repository.DebtHistoryRepository;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kamil on 1/17/14.
 */
@Service
public class DebtEventService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private DebtEventRepository debtEventRepository;

    @Autowired
    private DebtHistoryRepository debtHistoryRepository;

    public List<DebtEvent> createEvent(User debtCreator, Debt debt, DebtEventType debtEventType){
        //TODO: implement this
//        //DebtHistoryEntity debtHistoryEntity = mapper.map(debt, DebtEntity.class).getHistory();
//        //debtHistoryEntity.updateTimestamp();
//        DebtEventEntity event = new DebtEventEntity();
//        event.setCreationTime(new Date());
//        UserEntity eventCreator = mapper.map(debtCreator, UserEntity.class);
//        eventCreator.updateTimestamp();
//        event.setEventCreator(eventCreator);
//        event.setEventType(DebtEventType.DEBT_ADDITION_REQUEST);
//        event = debtEventRepository.save(event);
//       // debtHistoryEntity.addEvent(event);
//        //debtHistoryEntity = debtHistoryRepository.save(debtHistoryEntity);
//        //TODO: debt events mapping
//        //debt.setEvents(de);
//
//        //List<DebtEvent> events = getConvertedDebtEvents(debtHistoryEntity);
        return new ArrayList<DebtEvent>();
    }

    private List<DebtEvent> getConvertedDebtEvents(DebtHistoryEntity debtHistoryEntity) {
        List<DebtEvent> events = new ArrayList<DebtEvent>();
        for(DebtEventEntity debtEvent : debtHistoryEntity.getEvents()){
            DebtEvent mapped = mapper.map(debtEvent, DebtEvent.class);
            events.add(mapped);
        }
        return events;
    }

}
