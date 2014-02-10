package com.kalandyk.android.debt.action;

import android.app.Activity;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */
public abstract class DebtAction {

    protected AbstractDebtActivity activity;

    public DebtAction(AbstractDebtActivity activity) {
        this.activity = activity;
    }

    public abstract String getDebtActionString();

    public abstract void executeAction(Debt debt);

    protected abstract void taskFinished(Debt debt);

    protected void requestDebtRepayingTask(Debt debt){
        //TODO: write this method
    }

    protected void deleteDebtTask(Debt debt){

    }

    protected void cancelDebtRepayingRequestTask(Debt debt) {

    }


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

}
