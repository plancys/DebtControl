package com.kalandyk.services;

import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtState;
import com.kalandyk.api.model.DebtType;
import com.kalandyk.api.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kamil on 11/30/13.
 */
public class ConfirmationService {

    private static ConfirmationService instance;

    private  List<Confirmation> confirmations;

    private ConfirmationService() {
        confirmations = new ArrayList<Confirmation>();
    }

    public static ConfirmationService getInstance() {
        if (instance == null) {
            instance = new ConfirmationService();
        }
        return instance;
    }

    public void addConfirmation(Confirmation confirmation){
        confirmations.add(confirmation);
    }

    public List<Confirmation> getConfirmationsForUser(User user) {


        //TODO: add builder?
        confirmations = new ArrayList<Confirmation>();

        //Mock
        confirmations.add(new Confirmation());
        confirmations.add(new Confirmation());
        confirmations.add(new Confirmation());
        confirmations.add(new Confirmation());

        return confirmations;
    }

    public void deleteConfirmation(Confirmation confirmation){
        confirmations.remove(confirmation);
    }



}
