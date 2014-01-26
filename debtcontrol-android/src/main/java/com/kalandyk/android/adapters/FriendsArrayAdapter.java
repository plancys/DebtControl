package com.kalandyk.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.kalandyk.R;
import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.User;

import java.util.List;

/**
 * Created by kamil on 1/26/14.
 */
public class FriendsArrayAdapter extends ArrayAdapter<User> {


    private LayoutInflater layoutInflater;
    private Activity activity;



    public FriendsArrayAdapter(Activity context, List<User> friends) {
        super(context, R.layout.list_row_debts, friends);
        this.activity = activity;
        layoutInflater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        view = layoutInflater.inflate(R.layout.list_row_friends, parent, false);

        TextView text = (TextView) view.findViewById(R.id.friend_name);
        text.setText(getItem(position).toString());

        Button chooseButton = (Button) view.findViewById(R.id.friend_choose);
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendsArrayAdapter.this.activity.onBackPressed();
            }
        });


        final User user = getItem(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendChosen(user);

            }
        });

        return view;
    }

    protected void friendChosen(User user) {

    }

}
