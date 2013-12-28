package com.kalandyk.debt.action;

import android.app.Activity;

import com.kalandyk.api.model.Debt;
import com.kalandyk.services.DebtService;

/**
 * Created by kamil on 12/22/13.
 */
public abstract class DebtAction {

    protected Activity activity;

    protected DebtService debtService;

    public DebtAction(Activity activity) {
        this.activity = activity;
        debtService = DebtService.getInstance();
    }

    public abstract String getDebtActionString();

    public abstract void executeAction(Debt debt);

}
