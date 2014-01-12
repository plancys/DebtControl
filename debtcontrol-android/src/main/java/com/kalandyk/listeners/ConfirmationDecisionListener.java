package com.kalandyk.listeners;

import com.kalandyk.api.model.Confirmation;

/**
 * Created by kamil on 12/30/13.
 */
public interface ConfirmationDecisionListener {

    void onAccept(Confirmation confirmation);

    void onReject(Confirmation confirmation);

}
