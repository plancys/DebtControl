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
import com.kalandyk.android.utils.SharedPreferencesBuilder;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;
import com.kalandyk.api.model.security.PasswordUtils;
import com.kalandyk.api.model.wrapers.Friends;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

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

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return null;
    }

    protected void login(String email, String decodedPassword) {
        progressDialog.show();
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail(email);
        userCredentials.setPassword(decodedPassword);
        new DownloadFilesTask().execute(userCredentials);
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

    private void finished(User user) {
        progressDialog.dismiss();
        AbstractDebtActivity activity = getAbstractDebtActivity();
        Log.d(AbstractDebtActivity.TAG, "TASK FINISHED!");
        if (user == null) {
            Log.d(AbstractDebtActivity.TAG, "Login failed.");
            Toast.makeText(activity, "Login failed. Incorrect login or password", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d(AbstractDebtActivity.TAG, "User = " + user.toString());
        cachedData.setLoggedUser(user);
        activity.replaceFragmentWithStackClearing(new DebtsListFragment());
    }

    private class DownloadFilesTask extends AbstractDebtTask<UserCredentials, Void, User> {
        @Override
        protected User doInBackground(UserCredentials... credentials) {
            Log.d(AbstractDebtActivity.TAG, "Started TASK");
            User user = null;
            try {
                SharedPreferencesBuilder sharedPreferencesBuilder = new SharedPreferencesBuilder(getDebtActivity());
                sharedPreferencesBuilder.saveCredentials(credentials[0]);
                cachedData.setCredentials(credentials[0]);
                ResponseEntity<User> responseUser = restTemplate
                        .exchange(urls.getEmailUrl(), HttpMethod.GET, getAuthHeaders(), User.class);
                user = responseUser.getBody();

                ResponseEntity<Friends> responseFriends = restTemplate
                        .exchange(urls.getUserFriendsUrl(), HttpMethod.GET, getAuthHeaders(), Friends.class);

                cachedData.setFriends(responseFriends.getBody().getFriends());
            } catch (Exception e) {
                Log.e(AbstractDebtActivity.TAG, e.getMessage(), e);
            }
            return user;

        }

        @Override
        protected void onPostExecute(User result) {
            Log.d(AbstractDebtActivity.TAG, "Downloading user data finished.");
            WelcomeFragment.this.finished(result);
        }

        @Override
        protected AbstractDebtActivity getDebtActivity() {
            return getAbstractDebtActivity();
        }
    }
}
