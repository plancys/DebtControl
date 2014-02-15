package com.kalandyk.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalandyk.R;
import com.kalandyk.android.adapters.AbstractArrayAdapter;

/**
 * Created by kamil on 1/19/14.
 */
public class RegisterFragment extends AbstractFragment {

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container) {
        View debtListItemView = inflater.inflate(R.layout.fragment_register, container, false);
        return debtListItemView;
    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return null;
    }
}
