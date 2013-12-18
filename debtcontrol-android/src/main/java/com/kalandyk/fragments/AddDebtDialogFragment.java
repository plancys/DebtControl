package com.kalandyk.fragments;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.kalandyk.R;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtState;
import com.kalandyk.api.model.DebtType;
import com.kalandyk.api.model.User;
import com.kalandyk.listeners.NewDebtListener;

import java.util.Date;

/**
 * Created by kamil on 12/2/13.
 */
public class AddDebtDialogFragment extends DialogFragment {

    private NewDebtListener newDebtListener;
    private Spinner debtTypeSpinner;
    private SeekBar amountSeekBar;
    private EditText amountEditText;
    private EditText personConnected;
    private EditText description;
    private Button addDebtButton;
    private Button cancelButton;

    public AddDebtDialogFragment() {

    }

    public NewDebtListener getNewDebtListener() {
        return newDebtListener;
    }

    public void setNewDebtListener(NewDebtListener newDebtListener) {
        this.newDebtListener = newDebtListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_add_debt, container);

        init(view);

        setDialogStyle();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.debt_types, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        debtTypeSpinner.setAdapter(adapter);

        Button addButton = (Button) view.findViewById(R.id.bt_add_debt);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newDebtListener != null) {
                    Debt debt = buildDebtFromDialog();
                    newDebtListener.newDebtAdded(debt);
                    AddDebtDialogFragment.this.dismiss();
                }
            }
        });

        amountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                amountEditText.setText(String.valueOf(200 * i / 100)  );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDebtDialogFragment.this.dismiss();
            }
        });



        return view;
    }

    private void init(View view) {
        debtTypeSpinner = (Spinner) view.findViewById(R.id.debt_type_spinner);

        amountSeekBar = (SeekBar) view.findViewById(R.id.sb_amount_add_debt);
        view.findViewById(R.id.et_person_conn_add_debt);

        description = (EditText) view.findViewById(R.id.et_description_add_debt);

        amountEditText = (EditText) view.findViewById(R.id.et_amount_add_debt);

        personConnected = (EditText) view.findViewById(R.id.et_person_conn_add_debt);

        addDebtButton = (Button) view.findViewById(R.id.bt_add_debt);

        cancelButton = (Button) view.findViewById(R.id.bt_cancel_debt);
    }

    private Debt buildDebtFromDialog() {
        Debt debt = new Debt();
        //TODO: to change because server time is accurate
        debt.setCreationDate(new Date());
        //TODO: find user
        User user = new User();
        user.setName(personConnected.getText().toString());

        debt.setDescription(description.getText().toString());

        debt.setAmount(Long.parseLong(amountEditText.getText().toString()));

        debt.setDebtType(debtTypeSpinner.getSelectedItemPosition() == 0 ? DebtType.DEBT_WITH_CONFIRMATION : DebtType.DEBT_WITHOUT_CONFIRMATION);

        debt.setDebtState(debtTypeSpinner.getSelectedItemPosition() == 0 ? DebtState.NOT_CONFIRMED_DEBT : DebtState.UNPAID_DEBT);

        return debt;
    }

    private void setDialogStyle() {
        Window window = getDialog().getWindow();
        //set transparent background
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setTitle("Add debt");
    }
}
