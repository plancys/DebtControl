package com.kalandyk.android.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.android.utils.PasswordDecoder;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;

import com.kalandyk.api.model.wrapers.Friends;

/**
 * Created by kamil on 1/19/14.
 */
public class WelcomeFragment extends AbstractFragment {

    private EditText loginEditText;
    private EditText passwordEditText;

    private Button loginButton;
    private Button registerButton;

    private ProgressDialog progressDialog;
    private DebtDataContainer cachedData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View debtListItemView = inflater.inflate(R.layout.fragment_welcome, container, false);
        cachedData = getAbstractDebtActivity().getCashedData();
        initUIItems(debtListItemView);

        return debtListItemView;
    }

    private void initUIItems(View view) {
        loginEditText = (EditText) view.findViewById(R.id.et_login);
        passwordEditText = (EditText) view.findViewById(R.id.et_password);
        loginButton = (Button) view.findViewById(R.id.bt_login);
        registerButton = (Button) view.findViewById(R.id.bt_register);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginEditText.getText().toString();
                String plainPassword = passwordEditText.getText().toString();
                String encodedPassword = PasswordDecoder.encodePassword(plainPassword);
                WelcomeFragment.this.login(login, encodedPassword);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Go to registration
            }
        });
        progressDialog = getAbstractDebtActivity().getProgressDialog("Application is logging to server");
    }

    protected void login(String login, String encodedPassword) {
        progressDialog.show();
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setLogin(login);
        userCredentials.setPassword(encodedPassword);
        new DownloadFilesTask().execute(userCredentials);
    }

    private void finished(User user) {
        progressDialog.dismiss();
        AbstractDebtActivity activity = getAbstractDebtActivity();
        Log.d(AbstractDebtActivity.TAG, "TASK FINISHED!");
        if(user == null){
            Log.d(AbstractDebtActivity.TAG, "Login failed.");
            Toast.makeText(activity, "Login failed. Incorrect login or password", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d(AbstractDebtActivity.TAG, "User = " + user.toString());
        cachedData.setLoggedUser(user);
        activity.replaceFragmentWithStackClearing(new DebtsListFragment());
    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return null;
    }

    private class DownloadFilesTask extends AbstractDebtTask<UserCredentials, Void, User> {
        @Override
        protected User doInBackground(UserCredentials... credentials) {
            Log.d(AbstractDebtActivity.TAG, "Started TASK");
            User user = null;
            try {
                user = restTemplate.postForObject(urls.getLoginUrl(), credentials[0], User.class);
                Friends friends = restTemplate.getForObject(urls.getUserFriendsUrl(user.getLogin()), Friends.class);
                cachedData.setFriends(friends.getFriends());
            } catch (Exception e) {
                Log.e(AbstractDebtActivity.TAG, e.getMessage(), e);
            }
            return user;

        }

        @Override
        protected void onPostExecute(User result) {
            Log.d(AbstractDebtActivity.TAG, "onPostExcecute");
            WelcomeFragment.this.finished(result);
        }

        @Override
        protected AbstractDebtActivity getDebtActivity() {
            return getAbstractDebtActivity();
        }
    }
}
