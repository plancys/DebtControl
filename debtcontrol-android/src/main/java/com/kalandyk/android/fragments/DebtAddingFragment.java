package com.kalandyk.android.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.listeners.NewDebtListener;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtState;
import com.kalandyk.api.model.DebtType;
import com.kalandyk.api.model.User;

import java.util.Date;

/**
 * Created by kamil on 1/26/14.
 */
public class DebtAddingFragment extends AbstractFragment {

    private Debt debt;


    private NewDebtListener newDebtListener;
    private Spinner debtTypeSpinner;
    private Spinner debtRoleSpinner;

    private SeekBar amountSeekBar;
    private EditText amountEditText;
    private EditText personConnected;
    private EditText description;

    private Button addPersonConnectedButton;
    private Button addDebtButton;
    private Button cancelButton;


    private User userConnected;
    private AbstractDebtActivity activity;

//    public void setNewDebtListener(NewDebtListener newDebtListener) {
//        this.newDebtListener = newDebtListener;
//    }

    public DebtAddingFragment(AbstractDebtActivity activity) {
        this.activity = activity;
    }

    public DebtAddingFragment(AbstractDebtActivity activity, Debt debt) {
        this.activity = activity;
        this.debt = debt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(AbstractDebtActivity.TAG, "onCreateView -> ADDING FRIEND");
        View view = inflater.inflate(R.layout.dialog_fragment_add_debt, container, false);

        initGuiObjectReferences(view);
        initSpinnersActions();
        initButtonsActions();
        initAmountSpinnerAction();

        if (debt != null) {
            loadGuiFromDebtObject();
        }

        return view;
    }

    private void initAmountSpinnerAction() {
        amountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                amountEditText.setText(String.valueOf(200 * i / 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initButtonsActions() {
        addPersonConnectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDebtFromGui();
                activity.replaceFragment(new FriendsFragment(activity, debt));
            }
        });

        addDebtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newDebtListener != null) {
                    buildDebtFromGui();
                    Debt debt = DebtAddingFragment.this.debt;
                    newDebtListener.newDebtAdded(DebtAddingFragment.this.debt);
                    dismiss();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initSpinnersActions() {
        ArrayAdapter<CharSequence> debtTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.debt_types, android.R.layout.simple_spinner_dropdown_item);
        debtTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        debtTypeSpinner.setAdapter(debtTypeAdapter);

        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.debt_roles, android.R.layout.simple_spinner_dropdown_item);
        debtTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        debtRoleSpinner.setAdapter(roleAdapter);
    }

    private void dismiss() {

    }

    private void initGuiObjectReferences(View view) {
        debtTypeSpinner = (Spinner) view.findViewById(R.id.debt_type_spinner);
        debtRoleSpinner = (Spinner) view.findViewById(R.id.debt_role_spinner);
        //debtUserConnectedSpinner = (Spinner) view.findViewById(R.id.debt_users_spinner);
        amountSeekBar = (SeekBar) view.findViewById(R.id.sb_amount_add_debt);
        view.findViewById(R.id.et_person_conn_add_debt);
        description = (EditText) view.findViewById(R.id.et_description_add_debt);
        amountEditText = (EditText) view.findViewById(R.id.et_amount_add_debt);
        personConnected = (EditText) view.findViewById(R.id.et_person_conn_add_debt);
        addDebtButton = (Button) view.findViewById(R.id.bt_add_debt);
        cancelButton = (Button) view.findViewById(R.id.bt_cancel_debt);
        addDebtButton = (Button) view.findViewById(R.id.bt_add_debt);
        addPersonConnectedButton = (Button) view.findViewById(R.id.bt_add_debt_friends);
    }

    private void loadGuiFromDebtObject() {

        User user = new User();
        User connectedPerson = debt.getConnectedPerson();
        if (connectedPerson != null) {
            personConnected.setText(connectedPerson.getLogin());
        }

        description.setText(debt.getDescription());

        amountEditText.setText(String.valueOf(debt.getAmount()));

        //TODO: it is awful
        debtTypeSpinner.setSelection(debt.getDebtType().equals(DebtType.DEBT_WITH_CONFIRMATION) ? 0 : 1);

        debtTypeSpinner.setSelection(debt.getDebtState().equals(DebtState.NOT_CONFIRMED_DEBT) ? 0 : 1);
    }

    private void buildDebtFromGui() {

        if (debt == null) {
            debt = new Debt();
        }
        //TODO: to change because server time is accurate
        debt.setCreationDate(new Date());
        //TODO: find user
        User user = new User();
        user.setLogin(personConnected.getText().toString());

        debt.setDescription(description.getText().toString());

        try {
            debt.setAmount(Long.parseLong(amountEditText.getText().toString()));
        } catch (Exception e) {
            debt.setAmount(0l);
        }

        debt.setDebtType(debtTypeSpinner.getSelectedItemPosition() == 0 ? DebtType.DEBT_WITH_CONFIRMATION : DebtType.DEBT_WITHOUT_CONFIRMATION);

        debt.setDebtState(debtTypeSpinner.getSelectedItemPosition() == 0 ? DebtState.NOT_CONFIRMED_DEBT : DebtState.UNPAID_DEBT);
    }

    protected void showFriendsFragment() {

    }

    protected void friendChosen(User user) {

    }
}
