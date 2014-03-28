package com.kalandyk.api.model.wrapers;

import com.kalandyk.api.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 1/22/14.
 */
public class Friends {

    private List<User> friends;

    public Friends() {

    }

    public Friends(List<User> friends) {
        this.friends = friends;
    }

    public List<User> getFriends() {
        if (friends == null) {
            friends = new ArrayList<User>();
        }
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
}
