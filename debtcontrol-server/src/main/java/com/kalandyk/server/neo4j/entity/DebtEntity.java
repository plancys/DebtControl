package com.kalandyk.server.neo4j.entity;

import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.Date;

/**
 * Created by kamil on 1/5/14.
 */
@NodeEntity
public class DebtEntity extends  AbstractEntity{

    private  String description;

    private Long amount;

    private Date creationDate;

//    private DebtStateEntity debtStateEntity;
//
//    private DebtTypeEntity debtTypeEntity;

    @RelatedTo(type = "HAS_DEBTOR", elementClass = UserEntity.class)
    private UserEntity debtor;

    @RelatedTo(type = "HAS_CREDITOR", elementClass = UserEntity.class)
    private UserEntity creditor;

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
}
      /*      private boolean isSelected;
    private DebtState debtState;
    private DebtType debtType;
    private String description;
    private Long amount;
    private User debtor;
    private User creditor;
    //Dates
    private Date creationDate; */