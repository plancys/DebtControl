package com.kalandyk.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.wrapers.Debts;
import com.kalandyk.api.model.wrapers.Friends;
import com.kalandyk.api.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 1/22/14.
 */
public class SharedPreferencesBuilder {

    private static final String CURRENT_USER_KEY = "current_user";
    private static final String FRIENDS_KEY = "friends";
    private static final String OFFLINE_DEBTS = "offline_debts";
    private static final String ONLINE_DEBTS = "online_debts";
    private static final String OFFLINE_ID = "offline_id";

    private Activity activity;
    private Gson gson;

    public SharedPreferencesBuilder(Activity activity) {
        this.activity = activity;
        gson = new Gson();
    }

    public Long generateOfflineDebtId(){
        SharedPreferences sharedPref = getSharedPreferences();
        long id = sharedPref.getLong(OFFLINE_ID, -1);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(OFFLINE_ID, id - 1);
        editor.commit();

        return id;
    }

    private void saveValue(String key, String value) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        if (value != null) {
            editor.putString(key, value);
        }
        editor.commit();
    }

    public void saveCurrentUser(User user) {
        if (user == null) {
            saveValue(CURRENT_USER_KEY, null);
            return;
        }
        String jsonValue = gson.toJson(user);
        Log.d(AbstractDebtActivity.TAG, "SAVING: " + jsonValue);
        saveValue(CURRENT_USER_KEY, jsonValue);
    }

    public void saveFriends(List<User> friends) {
        Friends friendsWrapper = new Friends(friends);
        String jsonValue = gson.toJson(friendsWrapper);
        Log.d(AbstractDebtActivity.TAG, "SAVING: " + jsonValue);
        saveValue(FRIENDS_KEY, jsonValue);
    }

    public List<User> loadFriends() {
        String friendsJson = getSharedPreferences().getString(FRIENDS_KEY, "");
        if (friendsJson.equals("")) {
            return null;
        }
        Friends friends = gson.fromJson(friendsJson, Friends.class);
        return friends.getFriends();
    }

    public void saveOfflineDebts(List<Debt> debts){
        Debts offlineDebtsWrapper = new Debts(debts);
        String jsonValue = gson.toJson(offlineDebtsWrapper);
        saveValue(OFFLINE_DEBTS, jsonValue);
    }

    public List<Debt> loadOfflineDebts() {
        String offlineDebtsJson = getSharedPreferences().getString(OFFLINE_DEBTS, "");
        if (offlineDebtsJson.equals("")) {
            return null;
        }
        Debts debts = gson.fromJson(offlineDebtsJson, Debts.class);
        return debts.getDebts();
    }


    public void saveOnlineDebts(List<Debt> debts){
        Debts onlineDebtsWrapper = new Debts(debts);
        String jsonValue = gson.toJson(onlineDebtsWrapper);
        saveValue(ONLINE_DEBTS, jsonValue);
    }

    public List<Debt> loadOnlineDebts() {
        String onlineDebtsJson = getSharedPreferences().getString(ONLINE_DEBTS, "");
        if (onlineDebtsJson.equals("")) {
            return null;
        }
        Debts debts = gson.fromJson(onlineDebtsJson, Debts.class);
        return debts.getDebts();
    }

    public User loadCurrentUser() {
        String userJson = getSharedPreferences().getString(CURRENT_USER_KEY, "");
        if (userJson.equals("")) {
            return null;
        }

        User user = gson.fromJson(userJson, User.class);
        return user;
    }


    private SharedPreferences getSharedPreferences() {
        return activity.getPreferences(Context.MODE_PRIVATE);
    }

    public List<Confirmation> loadConfirmations() {
        return new ArrayList<Confirmation>();
    }
}


