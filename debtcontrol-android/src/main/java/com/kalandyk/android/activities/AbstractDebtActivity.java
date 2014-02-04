package com.kalandyk.android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kalandyk.R;
import com.kalandyk.android.fragments.*;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.android.utils.DebtUrls;
import com.kalandyk.android.utils.SharedPreferencesBuilder;
import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;
import com.kalandyk.api.model.wrapers.Confirmations;
import com.kalandyk.api.model.wrapers.Debts;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public abstract class AbstractDebtActivity extends BaseAbstractActivity {

    public static final String TAG = "com.kalandyk.debtcontrol";

    //wrapper class for data cached in phone
    private DebtDataContainer cashedData;

    protected Fragment currentFragment;

    private Stack<Fragment> fragmentStack;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        fragmentStack = new Stack<Fragment>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        initNavigationDrawer();
        showProgressDialog();
        new LoadDataFromServerTask().execute(getCashedData());
    }

    private void showProgressDialog() {
        progressDialog = getProgressDialog(getString(R.string.progress_dialog_fetching_data));
        progressDialog.show();
    }

    public void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, false);
    }

    public void replaceFragmentWithStackClearing(Fragment fragment) {
        fragmentStack.clear();
        fragmentStack.push(fragment);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        currentFragment = fragment;
    }

    private void replaceFragment(Fragment fragment, boolean fragmentTakenFromFragmentsStack) {
        if (currentFragment != null && !fragmentTakenFromFragmentsStack) {

            Iterator<Fragment> iterator = fragmentStack.iterator();
            Fragment fragmentToRemove = null;
            while (iterator.hasNext()) {
                Fragment next = iterator.next();
                if (next.getClass().equals(fragment.getClass())) {
                    fragmentToRemove = next;
                }
            }
            if (fragmentToRemove != null) {
                fragmentStack.remove(fragmentToRemove);
            }


            fragmentStack.add(currentFragment);
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        currentFragment = fragment;
    }

    protected abstract Fragment getContentFragment();

    public void onMenuButtonClick(View view) {
        Button button = (Button) view;
        switch (button.getId()) {

            case R.id.menu_button_add:
                onAddDebtButtonClick();
                break;

            case R.id.menu_button_add_group_expense:
                onAddGroupExpenseButtonClick();
                break;

            case R.id.menu_button_confirmations:
                onConfirmationButtonClick();
                break;

            case R.id.menu_button_history:
                onHistoryButtonClick();
                break;
        }
    }

    private void onHistoryButtonClick() {
        replaceFragment(new HistoryFragment());
    }

    private void onAddGroupExpenseButtonClick() {
        replaceFragment(new AddGroupExpenseFragment());
    }

    public void onAddDebtButtonClick() {
        //TODO: implement this in better way
        DebtAddingFragment addDebtDialogFragment = new DebtAddingFragment(this);

        replaceFragment(addDebtDialogFragment);
    }

    private void onConfirmationButtonClick() {
        replaceFragment(new ConfirmationFragment());
    }

    @Override
    public void onBackPressed() {
        if (fragmentStack.empty()) {
            super.onBackPressed();
            return;
        }
        Fragment lastFragment = fragmentStack.pop();
        replaceFragment(lastFragment, true);
    }

    public DebtDataContainer getCashedData() {
        if(cashedData == null){
            cashedData = new DebtDataContainer(this);
        }
        return cashedData;
    }

    public void setCashedData(DebtDataContainer cashedData) {
        this.cashedData = cashedData;
    }

    public SharedPreferencesBuilder getSharedPreferencesBuilder() {
        return sharedPreferencesBuilder;
    }

    public ProgressDialog getProgressDialog(String message){
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(this.getString(R.string.progress_dialog_please_wait));
            progressDialog.setMessage(message != null ? message : this.getString(R.string.progress_dialog_default_message));
        }
        return progressDialog;
    }

    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new MappingJacksonHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }


    private class LoadDataFromServerTask extends AsyncTask<DebtDataContainer, Void, DebtDataContainer>{

        @Override
        protected void onPostExecute(DebtDataContainer result) {
            progressDialog.dismiss();
            if(result == null){
                replaceFragment(new WelcomeFragment());
                return;
            }
            replaceFragment(getContentFragment());
        }

        @Override
        protected DebtDataContainer doInBackground(DebtDataContainer... debtDataContainers) {
            try {
                DebtDataContainer cachedData = debtDataContainers[0];
                RestTemplate restTemplate = getRestTemplate();
                DebtUrls urls = new DebtUrls(AbstractDebtActivity.this);
                UserCredentials credentials = getUserCredentials(cachedData);
                if(credentials == null){
                    return null;
                }

                loadDebts(restTemplate, urls, credentials);
                loadConfirmations(restTemplate, urls, credentials);
                return cachedData;
            }catch (Exception e){
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        private void loadConfirmations(RestTemplate restTemplate, DebtUrls urls, UserCredentials credentials) {
            Confirmations confirmations = restTemplate.postForObject(urls.getUserConfirmationsUrl(), credentials, Confirmations.class);
            if(confirmations != null){
                cashedData.setConfirmations(confirmations.getConfirmationList());
            }
        }

        private void loadDebts(RestTemplate restTemplate, DebtUrls urls, UserCredentials credentials) {
            Debts debts = restTemplate.postForObject(urls.getUserDebtUrl(), credentials, Debts.class);
            if(debts != null){
                Log.d(TAG, debts.toString());
                cashedData.setDebts(debts.getDebts());
            }
        }

        private UserCredentials getUserCredentials(DebtDataContainer cachedData) {
            User loggedUser = cachedData.getLoggedUser();
            if(loggedUser == null){
                return null;
            }
            UserCredentials credentials = new UserCredentials();
            credentials.setLogin(loggedUser.getLogin());
            credentials.setPassword(loggedUser.getPassword());
            return credentials;
        }
    }
}

