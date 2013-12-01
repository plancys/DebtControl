package com.kalandyk.activities;

import android.app.Fragment;

import com.kalandyk.api.model.Debt;
import com.kalandyk.fragments.DebtsListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 11/30/13.
 */
public class MainActivity extends AbstractActivity {

    private List<Debt> debts;

    @Override
    protected Fragment getContentFragment() {

        debts = new ArrayList<Debt>();
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());
        debts.add(new Debt());

        return new DebtsListFragment();
    }
}
