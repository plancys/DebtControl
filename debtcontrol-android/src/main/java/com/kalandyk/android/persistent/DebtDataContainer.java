package com.kalandyk.android.persistent;

import android.app.Activity;

import android.util.Log;
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

    public Debt enrichDebt(Debt debt){
        boolean foundedInCache = false;
        for(Debt debtFromCache : debts){
            if(debtFromCache.getId() == debt.getId()){
                foundedInCache = true;
                return debtFromCache;
            }
        }
        if(!foundedInCache){
            Log.e(AbstractDebtActivity.TAG, "Debt not found");
            //TODO: temporary action
            return debts.get(0);
        }
        return null;
    }

    public User enrichUser(User user){
        boolean foundedInCache = false;
        for(User userFromCache : friends){
            if(userFromCache.getId() == user.getId()){
                foundedInCache = true;
                return userFromCache;
            }
        }
        if(!foundedInCache){
            Log.e(AbstractDebtActivity.TAG, "Debt not found");
            //TODO: correct this
            return friends.get(0);
        }
        return null;
    }



}
