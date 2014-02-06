package com.kalandyk.android.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.api.model.Confirmation;

import java.util.List;

/**
 * Created by kamil on 2/6/14.
 */
public abstract class AbstractArrayAdapter<T> extends ArrayAdapter<T> {

    private AbstractDebtActivity activity;

    public enum DataType {
        DEBTS,
        CONFIRMATIONS,
    }

    public AbstractArrayAdapter(AbstractDebtActivity context, int resource, List<T> objects) {
        super(context, resource, objects);
        this.activity = context;
    }

    protected abstract DataType getAdapterDataType();

    public void refreshDataInList(){
        switch (getAdapterDataType()){
            case CONFIRMATIONS:
                refreshConfirmations();
                break;
            case DEBTS:
                refreshDebts();
                break;
            default:
                //TODO: raise some exceptions
        }
        this.notifyDataSetChanged();
    }

    protected void refreshDebts(){
        DebtDataContainer data = activity.getCashedData();
        this.clear();
        for(int i=0; i < data.getDebts().size(); i++){
            insert((T) data.getDebts().get(i), i);
        }
    }


    protected void refreshConfirmations(){
        DebtDataContainer data = activity.getCashedData();
        this.clear();
        for(int i=0; i < data.getConfirmations().size(); i++){
            insert((T) data.getConfirmations().get(i), i);
        }
    }
}
