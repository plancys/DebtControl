package com.kalandyk.android.fragments;

import android.app.ProgressDialog;
import android.text.InputType;
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
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;

import com.kalandyk.api.model.security.PasswordUtils;
import com.kalandyk.api.model.wrapers.Friends;

/**
 * Created by kamil on 1/19/14.
 */
public class WelcomeFragment extends AbstractFragment {

    private EditText emailEditText;
    private EditText passwordEditText;

    private Button loginButton;
    private Button registerButton;

    private ProgressDialog progressDialog;
    private DebtDataContainer cachedData;

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container) {
        View debtListItemView = inflater.inflate(R.layout.fragment_welcome, container, false);
        cachedData = getAbstractDebtActivity().getCachedData();
        initUIItems(debtListItemView);
        return debtListItemView;
    }

    private void initUIItems(View view) {
        emailEditText = (EditText) view.findViewById(R.id.et_email);
        emailEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        passwordEditText = (EditText) view.findViewById(R.id.et_password);
        loginButton = (Button) view.findViewById(R.id.bt_login);
        registerButton = (Button) view.findViewById(R.id.bt_register);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = emailEditText.getText().toString();
                String plainPassword = passwordEditText.getText().toString();
                String decodedPassword = PasswordUtils.getHashFromPassword(plainPassword);
                WelcomeFragment.this.login(login, decodedPassword);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAbstractDebtActivity().replaceFragment(new RegisterFragment());
            }
        });
        progressDialog = getAbstractDebtActivity().getProgressDialog("Application is logging to server");
    }

    protected void login(String email, String encodedPassword) {
        progressDialog.show();
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail(email);
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
                user = restTemplate.postForObject(urls.getEmailUrl(), credentials[0], User.class);
                Friends friends = restTemplate.getForObject(urls.getUserFriendsUrl(user.getEmail()), Friends.class);
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
