package com.kalandyk.activities;

import android.app.Fragment;

import com.kalandyk.api.model.Debt;
import com.kalandyk.fragments.DebtsListFragment;
import com.kalandyk.fragments.DetailsFragment;

import java.util.List;

/**
 * Created by kamil on 11/30/13.
 */
public class MainActivity extends AbstractActivity {

    private List<Debt> debts;

    @Override
    protected Fragment getContentFragment() {
         Fragment fragment = new DebtsListFragment() {
            @Override
            protected void showDetailsFragment(Debt debt) {
                MainActivity.this.replaceFragment(new DetailsFragment(debt));
            }
        };
        return fragment;


    }
}
