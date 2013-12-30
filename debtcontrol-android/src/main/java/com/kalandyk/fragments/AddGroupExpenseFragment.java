package com.kalandyk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalandyk.R;

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