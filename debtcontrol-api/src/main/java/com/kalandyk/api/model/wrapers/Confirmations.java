package com.kalandyk.api.model.wrapers;

import com.kalandyk.api.model.Confirmation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 1/22/14.
 */
public class Confirmations {

    private List<Confirmation> confirmationList;

    public List<Confirmation> getConfirmationList() {
        if(confirmationList == null){
            confirmationList = new ArrayList<Confirmation>();
        }
        return confirmationList;
    }

    public void setConfirmationList(List<Confirmation> confirmationList) {
        this.confirmationList = confirmationList;
    }
}
