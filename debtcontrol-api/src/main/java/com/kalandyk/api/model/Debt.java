package com.kalandyk.api.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Debt {

    private Long id;
    private String description;
    private Long amount;
    //Dates
    private Date creationDate;
    private DebtState debtState;
    private DebtType debtType;
    private User debtor;
    private User creditor;

    private User creator;
    private User connectedPerson;

    private boolean isSelected;

    private Set<DebtEvent> events;

    public Debt() {
        events = new HashSet<DebtEvent>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getDebtor() {
        return debtor;
    }

    public void setDebtor(User debtor) {
        this.debtor = debtor;
    }

    public User getCreditor() {
        return creditor;
    }

    public void setCreditor(User creditor) {
        this.creditor = creditor;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    // ...

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void addEvent(DebtEvent debtEvent) {
        events.add(debtEvent);
    }

    //TODO: add property and setting it in proper places
    public User getCreator() {

        return creator;
    }

    public Set<DebtEvent> getEvents() {
        return events;
    }

    public void setEvents(Set<DebtEvent> events) {
        this.events = events;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getConnectedPerson() {
        return connectedPerson;
    }

    public void setConnectedPerson(User connectedPerson) {
        this.connectedPerson = connectedPerson;
    }
}
