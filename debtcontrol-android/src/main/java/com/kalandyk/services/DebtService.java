package com.kalandyk.services;

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
public class DebtService {

    private static DebtService instance;

    private  List<Debt> debts;

    private DebtService() {
        debts = new ArrayList<Debt>();
    }

    public static DebtService getInstance() {
        if (instance == null) {
            instance = new DebtService();
        }
        return instance;
    }

    public void addDebt(Debt debt){
        debts.add(debt);
    }

    public List<Debt> getDebtsForUser(User user) {


        //TODO: add builder?
        debts = new ArrayList<Debt>();

        //Mock
        for (int i = 0; i < 5; i++) {
            User creditor = new User();
            User debtor = new User();
            Debt debt = new Debt();

            if (i % 2 == 0) {
                debt.setAmount(25L);
                debt.setDescription("Dinner after work");
                debt.setCreationDate(new Date());
                debtor.setName("Me");
                creditor.setName("Johny");

                debt.setDebtor(debtor);
                debt.setCreditor(creditor);
                debt.setDebtState(DebtState.UNPAID_DEBT);
                debt.setDebtType(DebtType.DEBT_WITHOUT_CONFIRMATION);

            } else {
                debt.setAmount(25L);
                debt.setDescription("Dinner after work");
                debt.setCreationDate(new Date());
                debtor.setName("Me");
                creditor.setName("Johny");

                debt.setDebtor(debtor);
                debt.setCreditor(creditor);
                Date date = new Date();
                debt.setDebtState(date.getTime() % 2 == 0 ? DebtState.NOT_CONFIRMED_DEBT : DebtState.NOT_CONFIRMED_PAY_OFF_DEBT);
                debt.setDebtType(DebtType.DEBT_WITH_CONFIRMATION);
            }

            debts.add(debt);

        }

        return debts;
    }

}
