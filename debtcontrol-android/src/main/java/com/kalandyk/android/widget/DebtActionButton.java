package com.kalandyk.android.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
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

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        //params.width = ViewPager.LayoutParams.FILL_PARENT
        setLayoutParams(params);

    }
}
