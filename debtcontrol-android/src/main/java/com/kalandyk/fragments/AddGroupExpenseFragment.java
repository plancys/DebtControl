package com.kalandyk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kalandyk.R;
import com.kalandyk.activities.AbstractActivity;
import com.kalandyk.adapters.DebtsArrayAdapter;
import com.kalandyk.api.model.Debt;
import com.kalandyk.listeners.DebtItemAction;
import com.kalandyk.services.DebtService;

/**
 * Created by kamil on 12/1/13.
 */
public class AddGroupExpenseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View debtListItemView = inflater.inflate(R.layout.fragment_group_expense, container, false);
        return debtListItemView;
    }
}