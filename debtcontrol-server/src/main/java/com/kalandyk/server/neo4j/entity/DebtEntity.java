package com.kalandyk.server.neo4j.entity;

import com.kalandyk.api.model.DebtState;
import com.kalandyk.api.model.DebtType;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.Date;

/**
 * Created by kamil on 1/5/14.
 */
@NodeEntity
public class DebtEntity extends AbstractEntity {

    public static final String DEBT_CREDITOR_RELATION = "HAS_CREDITOR";
    public static final String DEBT_DEBTOR_RELATION = "HAS_DEBTOR";
    public static final String DEBT_HAS_HISTORY = "HAS_HISTORY";

    private String description;

    private Long amount;

    private Date creationDate;

    @RelatedTo(type = DEBT_DEBTOR_RELATION, elementClass = UserEntity.class, direction = Direction.OUTGOING)
    //@Fetch
    private UserEntity debtor;

    @RelatedTo(type = DEBT_CREDITOR_RELATION, elementClass = UserEntity.class, direction = Direction.OUTGOING)
    //@Fetch
    private UserEntity creditor;

    @RelatedTo(type = DEBT_HAS_HISTORY, elementClass = DebtHistoryEntity.class, direction = Direction.OUTGOING)
    //@Fetch
    private DebtHistoryEntity history;

    private DebtState debtState;

    private DebtType debtType;

    public DebtEntity() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public UserEntity getDebtor() {
        return debtor;
    }

    public void setDebtor(UserEntity debtor) {
        this.debtor = debtor;
    }

    public UserEntity getCreditor() {
        return creditor;
    }

    public void setCreditor(UserEntity creditor) {
        this.creditor = creditor;
    }

    public DebtState getDebtState() {
        return debtState;
    }

    public void setDebtState(DebtState debtState) {
        this.debtState = debtState;
    }

    public DebtType getDebtType() {
        return debtType;
    }

    public void setDebtType(DebtType debtType) {
        this.debtType = debtType;
    }

    public DebtHistoryEntity getHistory() {
        if (history == null) {
            history = new DebtHistoryEntity();
        }
        return history;
    }

    public void setHistory(DebtHistoryEntity history) {
        this.history = history;
    }

    public void addDebtEvent(DebtEventEntity event){
        if (history == null) {
            history = new DebtHistoryEntity();
        }
        history.addEvent(event);
    }
}
