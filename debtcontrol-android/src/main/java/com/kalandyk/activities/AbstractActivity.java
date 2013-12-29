package com.kalandyk.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kalandyk.R;
import com.kalandyk.api.model.Debt;
import com.kalandyk.fragments.AddDebtDialogFragment;
import com.kalandyk.fragments.AddGroupExpenseFragment;
import com.kalandyk.fragments.ConfirmationFragment;
import com.kalandyk.fragments.DebtsListFragment;
import com.kalandyk.fragments.HistoryFragment;
import com.kalandyk.listeners.NewDebtListener;
import com.kalandyk.services.DebtService;

import java.util.Stack;

public abstract class AbstractActivity extends BaseAbstractActivity {

    public static final String TAG = "com.kalandyk.debtcontrol";

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


    protected final void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, false);
    }

    private void replaceFragment(Fragment fragment, boolean fromStack) {
        if (currentFragment != null && !fromStack) {
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

    private void onAddDebtButtonClick() {
        AddDebtDialogFragment addDebtDialogFragment = new AddDebtDialogFragment();
        addDebtDialogFragment.show(getFragmentManager(), "ADD DEBT DIALOG");
        addDebtDialogFragment.setNewDebtListener(new NewDebtListener() {
            @Override
            public void newDebtAdded(Debt debt) {
                Log.d(TAG, "New debt added");
                DebtService instance = DebtService.getInstance();
                instance.addDebt(debt);
                if (currentFragment instanceof DebtsListFragment) {
                    ((DebtsListFragment) currentFragment).notifyDataChanged();
                }
            }
        });
    }

    private void onConfirmationButtonClick() {
        replaceFragment(new ConfirmationFragment());
    }

    @Override
    public void onBackPressed() {
        if (!fragmentStack.empty()) {
            Fragment lastFragment = fragmentStack.pop();
            replaceFragment(lastFragment, true);
        } else {
            super.onBackPressed();
        }
    }


}

