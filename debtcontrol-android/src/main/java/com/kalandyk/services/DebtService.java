package com.kalandyk.services;

import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.ConfirmationType;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtEvent;
import com.kalandyk.api.model.DebtEventType;
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

    private ConfirmationService confirmationService;

    private DebtService() {
        debts = new ArrayList<Debt>();
        confirmationService = ConfirmationService.getInstance();
    }

    public static DebtService getInstance() {
        if (instance == null) {
            instance = new DebtService();
        }
        return instance;
    }

    public void addDebt(Debt debt){
        DebtEvent debtEvent;
        if(debt.getDebtType().equals(DebtType.DEBT_WITH_CONFIRMATION)){
            Confirmation confirmation = new Confirmation(debt, debt.getCreator(), ConfirmationType.REQUEST_DEBT_ADDING);
            confirmationService.addConfirmation(confirmation);
             debtEvent = new DebtEvent(DebtEventType.DEBT_ADDITION_REQUEST, debt.getCreator());
        } else {
            debtEvent = new DebtEvent(DebtEventType.DEBT_SIMPLE_ADDITION, debt.getCreator());
        }
        debt.addEvent(debtEvent);
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
                debt.setDebtState(date.getTime() % 2 == 0 ? DebtState.NOT_CONFIRMED_DEBT : DebtState.CONFIRMED_DEBT_WITH_NO_CONFIRMED_REPAYMENT);
                debt.setDebtType(DebtType.DEBT_WITH_CONFIRMATION);
            }

            debts.add(debt);

        }

        return debts;
    }

    public void deleteDebt(Debt debt){
        debts.remove(debt);
    }


    public void requestDebtPayOff(Debt debt) {
        debt.setDebtState(DebtState.CONFIRMED_DEBT_WITH_NO_CONFIRMED_REPAYMENT);
        DebtEvent debtEvent = new DebtEvent(DebtEventType.DEBT_REQUEST_REPAY, new User());
        debt.addEvent(debtEvent);
        //TODO: send confirmation to right person
        Confirmation confirmation = new Confirmation(debt, new User(), ConfirmationType.REQUEST_DEBT_REPAYING);
        confirmationService.addConfirmation(confirmation);

    }

    public void cancelDebtRepayRequest(Debt debt) {
        //TODO: delegate this action in another place
        debt.setDebtState(DebtState.CONFIRMED_NOT_REPAID_DEBT);
        DebtEvent debtEvent = new DebtEvent(DebtEventType.DEBT_CANCEL_REPAY_REQUEST, new User());
        debt.addEvent(debtEvent);
        //TODO: delete proper Confirmation
        confirmationService.getLastConfirmationForDebtByType(debt, ConfirmationType.REQUEST_DEBT_REPAYING);
    }
}
