package com.kalandyk.services;

import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 11/30/13.
 */
public class DebtService {

    private static DebtService instance;

    private DebtService(){

    }

    public static DebtService getInstance(){
        if(instance == null){
            instance = new DebtService();
        }
        return instance;
    }

    public List<Debt> getDebtsForUser(User user){
        List<Debt> debts = new ArrayList<Debt>();
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());

        return debts;
    }

}
