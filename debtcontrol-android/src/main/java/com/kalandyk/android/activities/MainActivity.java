package com.kalandyk.android.activities;

import android.app.Fragment;

import com.kalandyk.android.fragments.WelcomeFragment;
import com.kalandyk.api.model.Debt;
import com.kalandyk.android.fragments.DebtsListFragment;
import com.kalandyk.android.fragments.DetailsFragment;

import java.util.List;

/**
 * Created by kamil on 11/30/13.
 */
public class MainActivity extends AbstractActivity {

    private List<Debt> debts;

    @Override
    protected Fragment getContentFragment() {

        prepareCachedData();

        if(getCashedData().getLoggedUser() == null){
            return new WelcomeFragment();
        }

         Fragment fragment = new DebtsListFragment() {
            @Override
            protected void showDetailsFragment(Debt debt) {
                MainActivity.this.replaceFragment(new DetailsFragment(debt));
            }
        };
        return fragment;
    }

    private void prepareCachedData() {

    }
}
