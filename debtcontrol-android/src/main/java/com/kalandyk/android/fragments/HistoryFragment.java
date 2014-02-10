package com.kalandyk.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalandyk.R;
import com.kalandyk.android.adapters.AbstractArrayAdapter;

/**
 * Created by kamil on 12/29/13.
 */
public class HistoryFragment extends AbstractFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View debtListItemView = inflater.inflate(R.layout.fragment_history, container, false);
        return debtListItemView;
    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return null;
    }
}
