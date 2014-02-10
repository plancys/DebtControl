package com.kalandyk.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
import com.kalandyk.android.adapters.FriendsArrayAdapter;
import com.kalandyk.android.listeners.AddingPersonToDebtListener;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;

/**
 * Created by kamil on 1/26/14.
 */
public class FriendsFragment extends AbstractFragment {

    private FriendsArrayAdapter friendsArrayAdapter;
    private DebtDataContainer debtDataContainer;

    private AbstractDebtActivity activity;
    private Debt debt;

    public FriendsFragment(AbstractDebtActivity activity, Debt debt){
        this.activity = activity;
        this.debt = debt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        debtDataContainer = getAbstractDebtActivity().getCashedData();
        View friendsView = inflater.inflate(R.layout.fragment_friend_list, container, false);
        friendsArrayAdapter = initFriendsAdapter();
        ListView friendsListView = (ListView) friendsView.findViewById(R.id.list_view_friends);
        friendsListView.setAdapter(friendsArrayAdapter);
        return friendsView;
    }

    private FriendsArrayAdapter initFriendsAdapter() {
        return new FriendsArrayAdapter(getAbstractDebtActivity(), debtDataContainer.getFriends()) {
            @Override
            protected void friendChosen(User user) {
                debt.setConnectedPerson(user);
                activity.replaceFragment(new DebtAddingFragment(activity, debt));
            }
        };
    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return friendsArrayAdapter;
    }
}
