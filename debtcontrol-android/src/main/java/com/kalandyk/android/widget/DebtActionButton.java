package com.kalandyk.android.widget;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import com.kalandyk.R;

/**
 * Created by kamil on 2/15/14.
 */
public class DebtActionButton extends Button{


    public DebtActionButton(Context context, String text) {
        super(context, null, R.style.ButtonInListStyleGrey);
        //button.setLayoutParams(detailsButton.getLayoutParams());
        setBackgroundResource(R.drawable.button_grey);
        setText(text);
        setTextSize(12);
        setGravity(Gravity.CENTER);
    }
}
