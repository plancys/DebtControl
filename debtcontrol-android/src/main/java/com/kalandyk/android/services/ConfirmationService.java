package com.kalandyk.android.services;

import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.ConfirmationType;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtState;
import com.kalandyk.api.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 11/30/13.
 */
public class ConfirmationService {

    private static ConfirmationService instance;

    private List<Confirmation> confirmations;


    private ConfirmationService() {
        confirmations = new ArrayList<Confirmation>();
    }

    public static ConfirmationService getInstance() {
        if (instance == null) {
            instance = new ConfirmationService();
        }
        return instance;
    }

    public void addConfirmation(Confirmation confirmation) {
        confirmations.add(confirmation);
    }

    public List<Confirmation> getConfirmationsForUser(User user) {

        return confirmations;
    }

    public void deleteConfirmation(Confirmation confirmation) {
        confirmations.remove(confirmation);
    }


    public void getLastConfirmationForDebtByType(Debt debt, ConfirmationType requestDebtRepaying) {
        //TODO: retrieve proper confirmation
    }

    public void accept(Confirmation confirmation) {
        Debt connectedDebt = confirmation.getConnectedDebt();
        ConfirmationType confirmationType = confirmation.getConfirmationType();

        if (confirmationType.equals(ConfirmationType.REQUEST_DEBT_ADDING)) {
            connectedDebt.setDebtState(DebtState.CONFIRMED_NOT_REPAID_DEBT);
        } else if (confirmationType.equals(ConfirmationType.REQUEST_DEBT_REPAYING)) {
            connectedDebt.setDebtState(DebtState.CONFIRMED_REPAID_DEBT);
        }
        deleteConfirmation(confirmation);
    }

    public void reject(Confirmation confirmation) {
        Debt connectedDebt = confirmation.getConnectedDebt();
        ConfirmationType confirmationType = confirmation.getConfirmationType();

        if (confirmationType.equals(ConfirmationType.REQUEST_DEBT_ADDING)) {
            connectedDebt.setDebtState(DebtState.DELETED);
        } else if (confirmationType.equals(ConfirmationType.REQUEST_DEBT_REPAYING)) {
            connectedDebt.setDebtState(DebtState.CONFIRMED_NOT_REPAID_DEBT);
        }
        deleteConfirmation(confirmation);
    }
}
