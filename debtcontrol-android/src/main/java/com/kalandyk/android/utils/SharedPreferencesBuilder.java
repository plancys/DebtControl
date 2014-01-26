package com.kalandyk.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.Debt;
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

    private Activity activity;
    private Gson gson;

    public SharedPreferencesBuilder(Activity activity) {
        this.activity = activity;
        gson = new Gson();
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
        Friends friendsWrapper = new Friends();
        friendsWrapper.setFriends(friends);
        String jsonValue = gson.toJson(friendsWrapper);
        Log.d(AbstractDebtActivity.TAG, "SAVING: " + jsonValue);
        saveValue(FRIENDS_KEY, jsonValue);
    }

    public List<User> loadFriends() {
        String friendsJson = getSharedPreferences().getString(FRIENDS_KEY, "");
        Gson gson = new Gson();
        if (friendsJson.equals("")) {
            return null;
        }
        Friends friends = gson.fromJson(friendsJson, Friends.class);
        return friends.getFriends();
    }

    public User loadCurrentUser() {
        String userJson = getSharedPreferences().getString(CURRENT_USER_KEY, "");
        if (userJson.equals("")) {
            return null;
        }

        User user = gson.fromJson(userJson, User.class);
        return user;
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

    private SharedPreferences getSharedPreferences() {
        return activity.getPreferences(Context.MODE_PRIVATE);
    }

    public List<Debt> loadDebts() {
        return new ArrayList<Debt>();
    }

    public List<Confirmation> loadConfirmations() {
        return new ArrayList<Confirmation>();
    }
}


