package com.kalandyk.android.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
import com.kalandyk.android.utils.DebtUrls;
import com.kalandyk.api.model.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * Created by kamil on 1/26/14.
 */
public class DebtAddingFragment extends AbstractFragment {

    private final static int DEBT_WITH_CONFIRMATION_SPINNER_ITEM = 0;
    private final static int DEBT_WITHOUT_CONFIRMATION_SPINNER_ITEM = 1;

    private Debt debt;

    private Spinner debtTypeSpinner;
    private Spinner debtRoleSpinner;

    private SeekBar amountSeekBar;
    private EditText amountEditText;
    private EditText personConnected;
    private EditText description;

    private Button addPersonConnectedButton;
    private Button addDebtButton;
    private Button cancelButton;

    private AbstractDebtActivity activity;

    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    private LinearLayout personConnectedLayout;

    public DebtAddingFragment(AbstractDebtActivity activity) {
        this.activity = activity;
    }

    public DebtAddingFragment(AbstractDebtActivity activity, Debt debt) {
        this.activity = activity;
        this.debt = debt;
    }


    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container) {
        Log.d(AbstractDebtActivity.TAG, "onCreateView -> ADDING FRIEND");
        View view = inflater.inflate(R.layout.fragment_add_debt, container, false);

        initGuiObjectReferences(view);
        initSpinnersActions();
        initButtonsActions();
        initAmountSpinnerAction();

        setDebtTypeChangeListener();

        if (debt != null) {
            loadGuiFromDebtObject();
        }

        return view;
    }

    private void setDebtTypeChangeListener() {
        debtTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int selectedItem, long arg3) {
                personConnectedLayout.setVisibility(selectedItem == DEBT_WITH_CONFIRMATION_SPINNER_ITEM ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //optionally do something here
            }
        });
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
                saveNewDebt();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void saveNewDebt() {
        progressDialog.show();
        buildDebtFromGui();
        if (debtTypeSpinner.getSelectedItemPosition() == DEBT_WITH_CONFIRMATION_SPINNER_ITEM) {
            new SaveDebtTask().execute(debt);
        } else {
            debtAddedCallback(debt);
        }
    }

    private void initSpinnersActions() {
        ArrayAdapter<CharSequence> debtTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.debt_types, android.R.layout.simple_spinner_dropdown_item);
        debtTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        debtTypeSpinner.setAdapter(debtTypeAdapter);

        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.debt_roles, android.R.layout.simple_spinner_dropdown_item);
        debtTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        debtRoleSpinner.setAdapter(roleAdapter);
    }

    protected void dismiss() {
        activity.replaceFragment(new DebtsListFragment());
    }

    private void initGuiObjectReferences(View view) {
        debtTypeSpinner = (Spinner) view.findViewById(R.id.debt_type_spinner);
        debtRoleSpinner = (Spinner) view.findViewById(R.id.debt_role_spinner);
        amountSeekBar = (SeekBar) view.findViewById(R.id.sb_amount_add_debt);
        description = (EditText) view.findViewById(R.id.et_description_add_debt);
        amountEditText = (EditText) view.findViewById(R.id.et_amount_add_debt);
        personConnected = (EditText) view.findViewById(R.id.et_person_conn_add_debt);
        addDebtButton = (Button) view.findViewById(R.id.bt_add_debt);
        cancelButton = (Button) view.findViewById(R.id.bt_cancel_debt);
        addDebtButton = (Button) view.findViewById(R.id.bt_add_debt);
        addPersonConnectedButton = (Button) view.findViewById(R.id.bt_add_debt_friends);
        progressDialog = getAbstractDebtActivity().getProgressDialog(activity.getString(R.string.progress_dialog_saving_debt));
        alertDialog = getAbstractDebtActivity().getAlertDialog("");
        personConnectedLayout = (LinearLayout) view.findViewById(R.id.ll_person_connected);

    }

    private void loadGuiFromDebtObject() {
        User connectedPerson = debt.getConnectedPerson();
        if (connectedPerson != null) {
            personConnected.setText(connectedPerson.getEmail());
        }

        description.setText(debt.getDescription());
        amountEditText.setText(String.valueOf(debt.getAmount()));
        //TODO: it is awful
        debtTypeSpinner.setSelection(debt.getDebtType().equals(DebtType.DEBT_WITH_CONFIRMATION)
                ? DEBT_WITH_CONFIRMATION_SPINNER_ITEM : DEBT_WITHOUT_CONFIRMATION_SPINNER_ITEM);
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
        user.setEmail(personConnected.getText().toString());

        debt.setDescription(description.getText().toString());

        try {
            debt.setAmount(Long.parseLong(amountEditText.getText().toString()));
        } catch (Exception e) {
            debt.setAmount(0l);
        }

        debt.setDebtType(debtTypeSpinner.getSelectedItemPosition() == DEBT_WITH_CONFIRMATION_SPINNER_ITEM
                ? DebtType.DEBT_WITH_CONFIRMATION : DebtType.DEBT_WITHOUT_CONFIRMATION);

        debt.setDebtState(debtTypeSpinner.getSelectedItemPosition() == 0
                ? DebtState.NOT_CONFIRMED_DEBT : DebtState.UNPAID_DEBT);

        User loggedUser = cachedData.getLoggedUser();
        if (debtRoleSpinner.getSelectedItemPosition() == 0) {
            //debtor
            debt.setDebtor(loggedUser);
            debt.setCreditor(debt.getConnectedPerson());
            debt.setDebtPosition(DebtPosition.DEBTOR);
        } else {
            debt.setCreditor(loggedUser);
            debt.setDebtor(debt.getConnectedPerson());
            debt.setDebtPosition(DebtPosition.CREDITOR);
        }
        debt.setCreator(loggedUser);
    }

    protected void showFriendsFragment() {

    }

    protected void friendChosen(User user) {

    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return null;
    }

    private class SaveDebtTask extends AsyncTask<Debt, Void, Debt> {

        @Override
        protected Debt doInBackground(Debt... credentials) {
            Log.d(AbstractDebtActivity.TAG, "Started TASK");
            Debt addedDebt = null;
            try {
                addedDebt = saveDebtTask(addedDebt);

            } catch (Exception e) {
                handleAddingDebtError(e);
            }
            return addedDebt;

        }

        private Debt saveDebtTask(Debt addedDebt) {
            RestTemplate restTemplate = activity.getRestTemplate();
            DebtUrls urls = new DebtUrls(activity);
            addedDebt = restTemplate.postForObject(urls.getAddDebtUrl(), debt, Debt.class);
            if (cachedData.getLoggedUser().equals(addedDebt.getDebtor())) {
                addedDebt.setDebtPosition(DebtPosition.DEBTOR);
            } else {
                addedDebt.setDebtPosition(DebtPosition.CREDITOR);
            }

            return addedDebt;
        }

        @Override
        protected void onPostExecute(Debt result) {
            Log.d(AbstractDebtActivity.TAG, "onPostExcecute");
            debtAddedCallback(result);
        }

    }

    private void debtAddedCallback(Debt result) {
        progressDialog.dismiss();
        if (result != null) {
            saveDebtToCache(result);
        }
        dismiss();
    }

    private void saveDebtToCache(Debt result) {
        if (result.getDebtType().equals(DebtType.DEBT_WITH_CONFIRMATION)) {
            cachedData.addOnlineDebt(result);
        } else {
            result.setId(getAbstractDebtActivity().generateOfflineDebtId());
            cachedData.addOfflineDebt(result);
        }
    }

    private void handleAddingDebtError(final Exception e) {
        progressDialog.dismiss();
        getAbstractDebtActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(AbstractDebtActivity.TAG, e.getMessage(), e);
                alertDialog.setMessage(e.getMessage());
                alertDialog.show();
            }
        });

    }
}
