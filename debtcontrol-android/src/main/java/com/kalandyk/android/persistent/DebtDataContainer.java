package com.kalandyk.android.persistent;

import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.utils.SharedPreferencesBuilder;
import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtState;
import com.kalandyk.api.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 1/19/14.
 */
public class DebtDataContainer {

    private AbstractDebtActivity activity;

    private SharedPreferencesBuilder sharedPreferencesBuilder;

    private User loggedUser;

    //debts with confirmation
    private List<Debt> offlineDebts;
    //debts without confirmation
    private List<Debt> onlineDebts;

    private List<Confirmation> confirmations;

    private List<User> friends;
    private List<Debt> debts;

    public DebtDataContainer(AbstractDebtActivity activity) {
        this.activity = activity;
        this.sharedPreferencesBuilder = new SharedPreferencesBuilder(activity);
        initializeData();
    }

    private void initializeData() {
        setLoggedUser(sharedPreferencesBuilder.loadCurrentUser());
        setFriends(sharedPreferencesBuilder.loadFriends());
        //TODO: add synchronization with server
        //setOnlineDebts(sharedPreferencesBuilder.loadOnlineDebts());
        setOfflineDebts(sharedPreferencesBuilder.loadOfflineDebts());
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

    public List<Debt> getOnlineDebts() {
        if(onlineDebts == null){
            onlineDebts = new ArrayList<Debt>();
        }
        removeDeletedDebts(onlineDebts);
        return onlineDebts;
    }

    private void removeDeletedDebts(List<Debt> onlineDebts) {
        List<Debt> toDelete = new ArrayList<Debt>();
        for(Debt debt : onlineDebts){
            if(debt.getDebtState().equals(DebtState.DELETED))
                toDelete.add(debt);
        }

        for(Debt debt : toDelete){
            onlineDebts.remove(debt);
        }
    }

    public void setOnlineDebts(List<Debt> onlineDebts) {
        this.onlineDebts = onlineDebts;
    }

    public List<Confirmation> getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(List<Confirmation> confirmations) {
        this.confirmations = confirmations;
       //TODO: activity.setNotificationCounter(getConfirmations().size());
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
        sharedPreferencesBuilder.saveFriends(friends);
    }

    public List<Debt> getOfflineDebts() {
        if(offlineDebts == null){
            offlineDebts = new ArrayList<Debt>();
        }
        return offlineDebts;
    }

    public void setOfflineDebts(List<Debt> offlineDebts) {
        this.offlineDebts = offlineDebts;
    }

    public List<Debt> getDebts() {
        if(debts == null){
            debts = new ArrayList<Debt>();
        }
        debts.clear();
        debts.addAll(getOnlineDebts());
        debts.addAll(getOfflineDebts());
        removeDeletedDebts(debts);
        return debts;
    }

    public void addOnlineDebt(Debt result) {
        getOnlineDebts().add(result);
        sharedPreferencesBuilder.saveOnlineDebts(getOnlineDebts());
    }

    public void addOfflineDebt(Debt result) {
        getOfflineDebts().add(result);
        sharedPreferencesBuilder.saveOfflineDebts(getOfflineDebts());
    }
}
