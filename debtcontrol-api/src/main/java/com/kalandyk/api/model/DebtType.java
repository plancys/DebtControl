package com.kalandyk.api.model;

/**
 * Created by kamil on 12/17/13.
 */
public enum DebtType {



    DEBT_WITH_CONFIRMATION(0),

    DEBT_WITHOUT_CONFIRMATION(1);

    int code;

    private DebtType(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }


}
