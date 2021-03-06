package com.kalandyk.android.debt.action;

import android.app.Activity;
import android.app.ProgressDialog;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.listeners.DebtActionListener;
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */
public abstract class DebtAction {

    protected AbstractDebtActivity activity;
    protected ProgressDialog progressDialog;
    protected DebtActionListener debtActionListener;

    public DebtAction(AbstractDebtActivity activity) {
        this.activity = activity;
        progressDialog = activity.getProgressDialog("Sending data to server");
    }

    public DebtAction(DebtAction copy) {
        this.activity = copy.activity;
        this.progressDialog = copy.progressDialog;
        //progressDialog = activity.getProgressDialog("Sending data to server");
    }

    public abstract String getDebtActionButtonText();

    public abstract void executeAction(Debt debt);

    protected abstract void taskFinished(Debt debt);

    protected void requestDebtRepayingTask(Debt debt){
        //TODO: write this method
    }

    protected void deleteDebtTask(Debt debt){

    }

    protected void cancelDebtRepayingRequestTask(Debt debt) {

    }

    //protected abstract Debt changeDebtState(Debt debt);


    private class DebtActionTask extends AbstractDebtTask<Debt, Void, Debt>{

        public DebtActionTask(String url){

        }

        @Override
        protected AbstractDebtActivity getDebtActivity() {
            return activity;
        }

        @Override
        protected Debt doInBackground(Debt... debts) {
            return null;
        }
    }

    public void setDebtActionListener(DebtActionListener debtActionListener) {
        this.debtActionListener = debtActionListener;
    }

}
