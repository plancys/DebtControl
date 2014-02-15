package com.kalandyk.android.activities;

import com.kalandyk.android.fragments.AbstractFragment;
import com.kalandyk.android.fragments.WelcomeFragment;
import com.kalandyk.api.model.Debt;
import com.kalandyk.android.fragments.DebtsListFragment;
import com.kalandyk.android.fragments.DetailsFragment;

import java.util.List;

/**
 * Created by kamil on 11/30/13.
 */
public class MainActivityDebt extends AbstractDebtActivity {

    private List<Debt> debts;

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


}
