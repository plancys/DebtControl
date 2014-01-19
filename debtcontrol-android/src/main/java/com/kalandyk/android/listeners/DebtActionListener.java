package com.kalandyk.android.listeners;

import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */
public interface DebtActionListener {

    void onDetails(Debt debt);

    void onChangeDebtState(Debt debt);

}
