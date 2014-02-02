package com.kalandyk.android.activities;

import android.app.Fragment;

import com.kalandyk.android.fragments.WelcomeFragment;
import com.kalandyk.android.persistent.DebtDataContainer;
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

    @Override
    protected Fragment getContentFragment() {

        if(getCashedData().getLoggedUser() == null){
            return new WelcomeFragment();
        }

         Fragment fragment = new DebtsListFragment() {
            @Override
            protected void showDetailsFragment(Debt debt) {
                MainActivityDebt.this.replaceFragment(new DetailsFragment(debt));
            }
        };
        return fragment;
    }


}
