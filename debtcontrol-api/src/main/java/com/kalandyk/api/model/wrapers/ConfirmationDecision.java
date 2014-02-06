package com.kalandyk.api.model.wrapers;

import com.kalandyk.api.model.Confirmation;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Created by kamil on 2/6/14.
 */
public class ConfirmationDecision {

    private Confirmation confirmation;
    private Boolean decision;

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Confirmation confirmation) {
        this.confirmation = confirmation;
    }

    public Boolean getDecision() {
        return decision;
    }

    public void setDecision(Boolean decision) {
        this.decision = decision;
    }
}
