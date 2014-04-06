package com.kalandyk.android.fragments;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
import com.kalandyk.android.adapters.FriendsArrayAdapter;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class FriendsFragment extends AbstractFragment {

    private FriendsArrayAdapter friendsArrayAdapter;
    private DebtDataContainer debtDataContainer;
    private AbstractDebtActivity activity;
    private Debt debt;
    private Button findButton;
    private LinearLayout finderLayout;
    private ProgressDialog progressDialog;
    private EditText searchedEmail;


    public FriendsFragment(AbstractDebtActivity activity, Debt debt) {
        this.activity = activity;
        this.debt = debt;
        progressDialog = activity.getProgressDialog("Searching");
    }

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container) {
        debtDataContainer = getAbstractDebtActivity().getCachedData();
        View friendsView = inflater.inflate(R.layout.fragment_friend_list, container, false);
        initUIItems(friendsView);
        friendsArrayAdapter = initFriendsAdapter();
        ListView friendsListView = (ListView) friendsView.findViewById(R.id.list_view_friends);
        friendsListView.setAdapter(friendsArrayAdapter);
        return friendsView;
    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return friendsArrayAdapter;
    }

    private void initUIItems(View view) {
        finderLayout = (LinearLayout) view.findViewById(R.id.ll_friends_find);
        findButton = (Button) view.findViewById(R.id.bt_find_friend);
        searchedEmail = (EditText) view.findViewById(R.id.et_email_to_find);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                new FindFriendTask().execute(searchedEmail.getText().toString());

            }
        });
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

    private void addUserToList(User user) {
        if (user != null) {
            friendsArrayAdapter.insert(user, 0);
            friendsArrayAdapter.notifyDataSetChanged();
        }
    }

    private class FindFriendTask extends AbstractDebtTask<String, Void, User> {

        @Override
        protected AbstractDebtActivity getDebtActivity() {
            return getAbstractDebtActivity();
        }

        @Override
        protected User doInBackground(String... strings) {
            try {
                ResponseEntity<User> exchange = restTemplate.exchange(urls.findUserByEmailUrl(), HttpMethod.POST, getAuthHeadersWithRequestObject(strings[0]), User.class);
                return exchange.getBody();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            Log.d(AbstractDebtActivity.TAG, "Downloaded : " + user);
            progressDialog.dismiss();
            addUserToList(user);

        }


    }
}
