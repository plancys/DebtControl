package com.kalandyk.android.activities;

import android.app.ProgressDialog;
import com.kalandyk.android.fragments.AbstractFragment;
import com.kalandyk.android.fragments.WelcomeFragment;
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.api.model.Debt;
import com.kalandyk.android.fragments.DebtsListFragment;
import com.kalandyk.android.fragments.DetailsFragment;
import com.kalandyk.api.model.User;

import java.util.List;

/**
 * Created by kamil on 11/30/13.
 */
public class MainActivityDebt extends AbstractDebtActivity {

    private List<Debt> debts;
    private ProgressDialog progressDialog;

    @Override
    protected AbstractFragment getContentFragment() {

        if(getCachedData().getLoggedUser() == null){
            return new WelcomeFragment();
        }

         AbstractFragment fragment = new DebtsListFragment() {
            @Override
            protected void showDetailsFragment(Debt debt) {
                MainActivityDebt.this.replaceFragment(new DetailsFragment(debt));
            }
        };
        return fragment;
    }


    @Override
    protected void refreshDataAction() {
        currentFragment.refreshData();



    }

//    private class RefreshTask extends AbstractDebtTask<Void, Void, Void>{
//
//        @Override
//        protected AbstractDebtActivity getDebtActivity() {
//            return MainActivityDebt.this;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            loadConfirmationsToCacheTask();
//            loadDebtsToCacheTask();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void v){
//            progressDialog.dismiss();
//            currentFragment.getFragmentArrayAdapter().refreshDataInList();
//        }
//    }


}
