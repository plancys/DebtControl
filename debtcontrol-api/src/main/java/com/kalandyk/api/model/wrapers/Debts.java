package com.kalandyk.api.model.wrapers;

import com.kalandyk.api.model.Debt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 1/22/14.
 */
public class Debts {

    private List<Debt> debts;

    public Debts(){

    }

    public Debts(List<Debt> debts){
        this.debts = debts;
    }

    public List<Debt> getDebts() {
        if(debts == null ) {
            debts = new ArrayList<Debt>();
        }
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }
}
