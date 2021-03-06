package com.kalandyk.android.adapters;

import android.util.Log;
import android.widget.ArrayAdapter;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.persistent.DebtDataContainer;

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
                refreshItems((List<T>) getCachedData().getConfirmations());
                break;
            case DEBTS:
                refreshItems((List<T>) getCachedData().getDebts());
                break;
            default:
                //TODO: raise some exceptions
        }

    }

    private void refreshItems(final List<T> list){
        String message = String.format("Refreshing list. Before = %d, Now = %d", AbstractArrayAdapter.this.getCount(), list.size());
        Log.d(AbstractDebtActivity.TAG, message);
        if(activity == null){
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractArrayAdapter.this.clear();
                    for (int i = 0; i < list.size(); i++) {
                        insert((T) list.get(i), i);
                    }
                } catch (Exception e) {
                    activity.getAlertDialog(e.getMessage());
                }
                AbstractArrayAdapter.this.notifyDataSetChanged();
            }
        });

    }

    private DebtDataContainer getCachedData(){
        return activity.getCachedData();
    }
}
