package com.kalandyk.android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kalandyk.R;
import com.kalandyk.android.fragments.*;
import com.kalandyk.android.listeners.AddingPersonToDebtListener;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.android.utils.SharedPreferencesBuilder;
import com.kalandyk.api.model.User;

import java.util.Iterator;
import java.util.Stack;

public abstract class AbstractDebtActivity extends BaseAbstractActivity {

    public static final String TAG = "com.kalandyk.debtcontrol";

    //wrapper class for data cached in phone
    private DebtDataContainer cashedData;

    protected Fragment currentFragment;

    private Stack<Fragment> fragmentStack;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        fragmentStack = new Stack<Fragment>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        initNavigationDrawer();

        replaceFragment(getContentFragment());
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
        DebtAddingFragment addDebtDialogFragment = new DebtAddingFragment(this) ;
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
        return cashedData;
    }

    public void setCashedData(DebtDataContainer cashedData) {
        this.cashedData = cashedData;
    }

    public SharedPreferencesBuilder getSharedPreferencesBuilder() {
        return sharedPreferencesBuilder;
    }
}

