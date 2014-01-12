package com.kalandyk.api.model;

/**
 * Created by kamil on 1/12/14.
 */
public class FriendshipRequest {
    private User requester;
    private User target;

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }
}
