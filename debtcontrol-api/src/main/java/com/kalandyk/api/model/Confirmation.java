package com.kalandyk.api.model;

/**
 * Created by kamil on 12/28/13.
 */
public class Confirmation {

    private Long id;

    private Debt connectedDebt;

    private User requestApplicant;

    private ConfirmationType confirmationType;

    public Confirmation(Debt connectedDebt, User requestApplicant, ConfirmationType confirmationType) {
        this.confirmationType = confirmationType;
        this.connectedDebt = connectedDebt;
        this.requestApplicant = requestApplicant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConfirmationType getConfirmationType() {
        return confirmationType;
    }

    public void setConfirmationType(ConfirmationType confirmationType) {
        this.confirmationType = confirmationType;
    }

    public User getRequestApplicant() {
        return requestApplicant;
    }

    public void setRequestApplicant(User requestApplicant) {
        this.requestApplicant = requestApplicant;
    }

    public Debt getConnectedDebt() {
        return connectedDebt;
    }

    public void setConnectedDebt(Debt connectedDebt) {
        this.connectedDebt = connectedDebt;
    }

}
