package com.kalandyk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kalandyk.R;
import com.kalandyk.adapters.ConfirmationsArrayAdapter;
import com.kalandyk.api.model.User;
import com.kalandyk.services.ConfirmationService;

/**
 * Created by kamil on 12/18/13.
 */
public class ConfirmationFragment extends Fragment {

    private ConfirmationService confirmationService;

    private ConfirmationsArrayAdapter adapter;

    public ConfirmationFragment(){
        confirmationService = ConfirmationService.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View confirmationListItemView = inflater.inflate(R.layout.fragment_confirmation_list, container, false);

        adapter = initDebtsArrayAdapter();

        initListView(confirmationListItemView);

        return confirmationListItemView;
    }

    private void initListView(View confirmationListItemView) {
        ListView listView = (ListView) confirmationListItemView.findViewById(R.id.confirmations_list_view);
        listView.setAdapter(adapter);
    }

    private ConfirmationsArrayAdapter initDebtsArrayAdapter() {
        return new ConfirmationsArrayAdapter(getActivity(), confirmationService.getConfirmationsForUser(new User()));
    }


}
