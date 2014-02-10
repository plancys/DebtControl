package com.kalandyk.android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;
import com.kalandyk.R;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
import com.kalandyk.android.fragments.*;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.android.utils.SharedPreferencesBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractDebtActivity extends BaseAbstractActivity {

    public static final String TAG = "com.kalandyk.debtcontrol";

    //wrapper class for data cached in phone
    private DebtDataContainer cashedData;

    protected AbstractFragment currentFragment;

    private Stack<Fragment> fragmentStack;

    private ProgressDialog progressDialog;

    private TextView confirmationAmountTextView;

    private ScheduledExecutorService scheduler;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        fragmentStack = new Stack<Fragment>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        initNavigationDrawer();
        showProgressDialog();
        new LoadDataFromServerTask().execute(getCashedData());
        confirmationAmountTextView = (TextView) findViewById(R.id.tv_notification_number);



        initScheduler();


    }

    private void initScheduler() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (getSchedulerTask(), 0, 1, TimeUnit.MINUTES);
    }

    private Runnable getSchedulerTask() {
        return new Runnable() {
            public void run() {
                Log.d(TAG, "----> Updating task executed");
                refreshLists();

            }
        };
    }

    private void refreshLists() {
        AbstractArrayAdapter fragmentArrayAdapter = currentFragment.getFragmentArrayAdapter();
        if(fragmentArrayAdapter != null){
            fragmentArrayAdapter.refreshDataInList();
        }
    }

    private void showProgressDialog() {
        progressDialog = getProgressDialog(getString(R.string.progress_dialog_fetching_data));
        progressDialog.show();
    }

    public void replaceFragment(AbstractFragment fragment) {
        replaceFragment(fragment, false);
        setNotificationCounter();
    }

    public void replaceFragmentWithStackClearing(AbstractFragment fragment) {
        fragmentStack.clear();
        fragmentStack.push(fragment);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        currentFragment = fragment;
    }

    private void replaceFragment(AbstractFragment fragment, boolean fragmentTakenFromFragmentsStack) {
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

    protected abstract AbstractFragment getContentFragment();

    public void onMenuButtonClick(View view) {
        Button button = (Button) view;
        switch (button.getId()) {

            case R.id.menu_button_add:
                DebtAddingFragment addDebtDialogFragment = new DebtAddingFragment(this);
                replaceFragment(addDebtDialogFragment);
                break;

            case R.id.menu_button_add_group_expense:
                replaceFragment(new AddGroupExpenseFragment());
                break;

            case R.id.menu_button_confirmations:
                replaceFragment(new ConfirmationFragment());
                break;

            case R.id.menu_button_debt_list:
                replaceFragment(new DebtsListFragment());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy(), shutdown scheduler() ");
        scheduler.shutdown();
    }

    @Override
    public void onResume(){
        super.onResume();
        boolean shutdown = scheduler.isShutdown();
        Log.d(TAG, "onResume(), scheduler isShutdown = " + shutdown);
        scheduler.scheduleAtFixedRate
                (getSchedulerTask(), 0, 1, TimeUnit.MINUTES);
    }

    public DebtDataContainer getCashedData() {
        if (cashedData == null) {
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

    public ProgressDialog getProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(this.getString(R.string.progress_dialog_please_wait));
            progressDialog.setMessage(message != null ? message : this.getString(R.string.progress_dialog_default_message));
        }
        return progressDialog;
    }

    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        //messageConverters.add(new FormHttpMessageConverter());
       // messageConverters.add(new StringHttpMessageConverter());
       // messageConverters.add(new MappingJacksonHttpMessageConverter());
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        //restTemplate.setMessageConverters(messageConverters);
        //restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        //RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public void setNotificationCounter() {
        int confirmationsAmount = cashedData.getConfirmations().size();
        confirmationAmountTextView = (TextView) this.findViewById(R.id.tv_notification_number);
        LinearLayout notificationLayout = (LinearLayout) findViewById(R.id.ll_notification_layout);
        if (notificationLayout == null) {
            return;
        }
        if (confirmationsAmount > 0) {
            notificationLayout.setVisibility(View.VISIBLE);
            confirmationAmountTextView.setText(String.valueOf(confirmationsAmount));
        } else {
            notificationLayout.setVisibility(View.GONE);
        }
    }


    private class LoadDataFromServerTask extends AbstractDebtTask<DebtDataContainer, Void, DebtDataContainer> {

        @Override
        protected void onPostExecute(DebtDataContainer result) {
            progressDialog.dismiss();
            if (result == null) {
                replaceFragment(new WelcomeFragment());
                return;
            }
            replaceFragment((AbstractFragment) getContentFragment());
        }

        @Override
        protected DebtDataContainer doInBackground(DebtDataContainer... debtDataContainers) {
            try {
                DebtDataContainer cachedData = debtDataContainers[0];
                if (getUserCredentials() == null) {
                    return null;
                }
                loadDebtsToCacheTask();
                loadConfirmationsToCacheTask();
                return cachedData;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected AbstractDebtActivity getDebtActivity() {
            return AbstractDebtActivity.this;
        }
    }


}

