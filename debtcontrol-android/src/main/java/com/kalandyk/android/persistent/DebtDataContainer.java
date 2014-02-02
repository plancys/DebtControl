package com.kalandyk.android.persistent;

import android.app.Activity;

import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.activities.MainActivityDebt;
import com.kalandyk.android.utils.SharedPreferencesBuilder;
import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;

import java.util.List;

/**
 * Created by kamil on 1/19/14.
 */
public class DebtDataContainer {

    private Activity activity;

    private SharedPreferencesBuilder sharedPreferencesBuilder;

    private User loggedUser;

    private List<Debt> debts;

    private List<Confirmation> confirmations;

    private List<User> friends;

    public DebtDataContainer(AbstractDebtActivity activity) {
        this.activity = activity;
        this.sharedPreferencesBuilder = new SharedPreferencesBuilder(activity);
        initializeData();
    }

    private void initializeData() {
        setLoggedUser(sharedPreferencesBuilder.loadCurrentUser());
        setFriends(sharedPreferencesBuilder.loadFriends());
        setDebts(sharedPreferencesBuilder.loadDebts());
        setConfirmations(sharedPreferencesBuilder.loadConfirmations());
    }

    private DebtDataContainer(){}

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
        sharedPreferencesBuilder.saveCurrentUser(loggedUser);
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public List<Confirmation> getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(List<Confirmation> confirmations) {
        this.confirmations = confirmations;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
        sharedPreferencesBuilder.saveFriends(friends);
    }



}
