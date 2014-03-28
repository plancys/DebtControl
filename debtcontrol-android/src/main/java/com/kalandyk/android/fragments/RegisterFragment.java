package com.kalandyk.android.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.security.PasswordUtils;

public class RegisterFragment extends AbstractFragment {

    private EditText email;
    private EditText login;
    private EditText password;
    private EditText passwordConfirmation;
    private Button registerButton;

    private ProgressDialog progressDialog;

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container) {
        View debtListItemView = inflater.inflate(R.layout.fragment_register, container, false);
        initUIItems(debtListItemView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString()
                        .equals(passwordConfirmation.getText().toString())) {
                    User user = prepareUserObject();
                    progressDialog.show();
                    new RegisterTask().execute(user);

                } else {

                }
            }
        });

        progressDialog = getAbstractDebtActivity().getProgressDialog("Registering...");

        return debtListItemView;
    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return null;
    }

    private User prepareUserObject() {
        User user = new User();
        user.setLogin(login.getText().toString());
        user.setEmail(email.getText().toString());
        String decodedPassword = PasswordUtils.getHashFromPassword(password.toString());
        user.setPassword(decodedPassword);
        return user;
    }

    private void initUIItems(View view) {
        email = (EditText) view.findViewById(R.id.et_email);
        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        login = (EditText) view.findViewById(R.id.et_login);
        password = (EditText) view.findViewById(R.id.et_password);
        passwordConfirmation = (EditText) view.findViewById(R.id.et_password_confirmation);
        registerButton = (Button) view.findViewById(R.id.bt_register);

    }

    private class RegisterTask extends AbstractDebtTask<User, Void, User>{

        @Override
        protected AbstractDebtActivity getDebtActivity() {
            return getAbstractDebtActivity();
        }

        @Override
        protected User doInBackground(User... users) {
            User user = users[0];
            User registeredUser = null;
            try {
                registeredUser = restTemplate.postForObject(urls.getRegisterUrl(), user, User.class);
            }catch (Exception e){
                Log.e(AbstractDebtActivity.TAG, e.getMessage(), e);
            }
            return registeredUser;
        }

        @Override
        protected void onPostExecute(User result) {
            Log.d(AbstractDebtActivity.TAG, "onPostExcecute");
            RegisterFragment.this.finished(result);
        }

    }

    private void finished(User result) {
        cachedData.setLoggedUser(result);
        progressDialog.dismiss();
        if(result != null){
            getAbstractDebtActivity().replaceFragmentWithStackClearing(new DebtsListFragment());
        } else {
            Toast.makeText(getAbstractDebtActivity(), "Register failed. Tu du du du..", Toast.LENGTH_LONG).show();
        }
    }
}
