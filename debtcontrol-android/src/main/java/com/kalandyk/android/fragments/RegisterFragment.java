package com.kalandyk.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalandyk.R;

/**
 * Created by kamil on 1/19/14.
 */
public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View debtListItemView = inflater.inflate(R.layout.fragment_register, container, false);
        return debtListItemView;
    }

}
