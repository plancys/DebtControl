package com.kalandyk.android.activities;

import android.app.ProgressDialog;
import com.kalandyk.android.fragments.AbstractFragment;
import com.kalandyk.android.fragments.DebtsListFragment;
import com.kalandyk.android.fragments.DetailsFragment;
import com.kalandyk.android.fragments.WelcomeFragment;
import com.kalandyk.api.model.Debt;

import java.util.List;

public class MainActivityDebt extends AbstractDebtActivity {

    private List<Debt> debts;
    private ProgressDialog progressDialog;

    @Override
    protected AbstractFragment getContentFragment() {

        if (getCachedData().getLoggedUser() == null) {
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
}
