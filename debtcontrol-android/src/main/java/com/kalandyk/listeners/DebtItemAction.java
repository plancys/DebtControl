package com.kalandyk.listeners;

import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */
public interface DebtItemAction {

    void onDetails(Debt debt);

    void onChangeDebtState(Debt debt);

}
