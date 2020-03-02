package tingtel.payment.fragments.transfer_airtime;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.text.DecimalFormat;
import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.activities.sign_up.SignUpActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.AppUtils.dialUssdCode;


public class TransferAirtimeFragment extends Fragment {

    Button btnCheckBalance;
    TextView tvGetTransferPin;
    EditText edAmount;
    Button btnNext;
    NavController navController;
    String UssdCode;
    String SimNetwork;
    int SimNo;
    String SimSerial;
    SessionManager sessionManager;
    AppDatabase appDatabase;
    private ImageView homeImageview, settingsImagview;
    private RadioGroup rdSimGroup;
    private LinearLayout backButtonLayout;
    private RadioButton rdSimButton;
    private RadioButton rdSim1, rdSim2;
    private String finalamount;
    private Boolean balanceChecked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_airtime, container, false);

//        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Transfer Airtime");

        initViews(view);
        initListeners(view);

        confirmSimRegistrations();
        populateSimRadioButtons();


        return view;
    }

    private void initViews(View view) {
        sessionManager = AppUtils.getSessionManagerInstance();
        backButtonLayout = view.findViewById(R.id.backArrowLayout);
        homeImageview = view.findViewById(R.id.homeImageview);
        settingsImagview = view.findViewById(R.id.settingsImageview);

        rdSimGroup = view.findViewById(R.id.radioSim);
        btnCheckBalance = view.findViewById(R.id.btn_check_balance);
        btnNext = view.findViewById(R.id.btn_next);
        edAmount = view.findViewById(R.id.ed_amount);
        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String input = s.toString();

                if (!input.isEmpty()) {

                    input = input.replace(",", "");

                    DecimalFormat format = new DecimalFormat("#,###,###");
                    String newPrice = format.format(Double.parseDouble(input));


                    edAmount.removeTextChangedListener(this); //To Prevent from Infinite Loop

                    edAmount.setText(newPrice);
                    edAmount.setSelection(newPrice.length()); //Move Cursor to end of String

                    edAmount.addTextChangedListener(this);
                }

            }

            @Override
            public void afterTextChanged(final Editable s) {
            }
        });


        rdSim1 = view.findViewById(R.id.radioSim1);
        rdSim2 = view.findViewById(R.id.radioSim2);

        appDatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getContext()));

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }

    private void initListeners(View view) {
        backButtonLayout.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        homeImageview.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        });

        settingsImagview.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));

        rdSimGroup.setOnCheckedChangeListener((group, checkedId) -> {
            //  Toast.makeText(getContext(), "testing for checked", Toast.LENGTH_LONG).show();
            // get selected radio button from radioGroup
            int selectedId = rdSimGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            rdSimButton = view.findViewById(selectedId);

            if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("mtn")) {
                SimNetwork = "Mtn";
            } else if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("air")) {
                SimNetwork = "Airtel";
            } else if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("glo")) {
                SimNetwork = "Glo";
            } else if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("9mo") ||
                    (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("eti"))) {
                SimNetwork = "9mobile";
            } else {

                return;
            }

            switch (selectedId) {
                case R.id.radioSim1:
                    SimNo = 0;
                    SimSerial = sessionManager.getSimSerialICCID();
                    break;
                case R.id.radioSim2:
                    SimNo = 1;
                    SimSerial = sessionManager.getSimSerialICCID1();
                    break;
                default:
                    return;
            }
        });


        btnCheckBalance.setOnClickListener(v -> {
            // get selected radio button from radioGroup
            int selectedId = rdSimGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(getContext(), "Select a sim first", Toast.LENGTH_SHORT).show();
                return;
            }
            // find the radiobutton by returned id
            rdSimButton = view.findViewById(selectedId);

            if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("mtn")) {
                UssdCode = "*556#";
            } else if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("air")) {
                UssdCode = "*123#";
            } else if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("glo")) {
                UssdCode = "*124*1#";
            } else if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("9mo") ||
                    (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("eti"))) {
                UssdCode = "*232#";
            } else {

                Toast.makeText(getActivity(), "Cant Check USSD Balance for this network", Toast.LENGTH_LONG).show();
                return;
            }

            dialUssdCode(getActivity(), UssdCode, SimNo);
            balanceChecked = true;
        });


        btnNext.setOnClickListener(v -> {


            if (!checkSelectedSim(view)) {
                return;
            }
            //if all fields and conditions are satisfied proceed.
            if (isValidAllFields()) {
                String input = edAmount.getText().toString().trim();
                finalamount = input.replace(",", "");

                Bundle bundle = new Bundle();
                bundle.putString("simNetwork", SimNetwork);
                bundle.putString("simSerial", "" + SimSerial);
                bundle.putInt("simNo", SimNo);
                bundle.putString("amount", finalamount);
                navController.navigate(R.id.action_transferAirtimeFragment2_to_transferAirtimeReceiverInfoFragment2, bundle);

            }
        });
    }

    private void confirmSimRegistrations() {
        String NoOfSIm = sessionManager.getSimStatus();

        switch (NoOfSIm) {

            case "NO SIM":
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                intent.putExtra("task", "registerSim1");
                startActivity(intent);

                break;

            case "SIM1":

                if (!sim1ExistsCheck()) {
                    Toast.makeText(getActivity(), "New Sim Detected, You Need to Register this sim on your account", Toast.LENGTH_LONG).show();
                    // navigateToSim1Register();
                    Intent intent2 = new Intent(getActivity(), SignUpActivity.class);
                    intent2.putExtra("task", "registerSim1");
                    startActivity(intent2);
                    return;
                }
                break;
            case "SIM1 SIM2":

                if (!sim1ExistsCheck()) {
                    Toast.makeText(getActivity(), "New Sim Detected, You Need to Register this sim on your account", Toast.LENGTH_LONG).show();
                    // navigateToSim1Register();
                    Intent intent3 = new Intent(getActivity(), SignUpActivity.class);
                    intent3.putExtra("task", "registerSim1");
                    startActivity(intent3);
                    return;
                }

                if (!sim2ExistsCheck()) {
                    Toast.makeText(getActivity(), "New Sim Detected, You Need to Register this sim on your account", Toast.LENGTH_LONG).show();
                    // navigateToSim2Register();
                    Intent intent4 = new Intent(getActivity(), SignUpActivity.class);
                    intent4.putExtra("task", "registerSim2");
                    startActivity(intent4);
                    return;
                }
                break;
        }
    }


    private boolean sim1ExistsCheck() {

        String Sim1Serial = sessionManager.getSimSerialICCID();

        return appDatabase.simCardsDao().getSerial(Sim1Serial).size() > 0;

    }

    private boolean sim2ExistsCheck() {

        String Sim2Serial = sessionManager.getSimSerialICCID1();

        return appDatabase.simCardsDao().getSerial(Sim2Serial).size() > 0;

    }


    private void populateSimRadioButtons() {
        String NoOfSIm = sessionManager.getSimStatus();

        switch (NoOfSIm) {
            case "SIM1":
                rdSim1.setVisibility(View.VISIBLE);
                rdSim2.setVisibility(View.GONE);
                rdSim1.setText(sessionManager.getNetworkName());
                break;

            case "SIM1 SIM2":
                rdSim1.setVisibility(View.VISIBLE);
                rdSim2.setVisibility(View.VISIBLE);
                rdSim1.setText(sessionManager.getNetworkName() + " (" + sessionManager.getSimPhoneNumber() + ")");
                rdSim2.setText(sessionManager.getNetworkName1() + " (" + sessionManager.getSimPhoneNumber1() + ")");
                break;
        }
    }

    private boolean isValidAllFields() {
        if (rdSimGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Please select a sim", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edAmount.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Amount is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edAmount.getText().toString().trim().length() < 3) {
            Toast.makeText(getContext(), "Amount is too low. Minimum amount is 100", Toast.LENGTH_SHORT).show();
            return false;

        }

        //todo: add not
        if (balanceChecked) {
            AppUtils.showDialog("Please Check Account Balance First", getActivity());
            return false;
        }

        return true;
    }


    private boolean checkSelectedSim(View v) {
        int selectedId = rdSimGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(getContext(), "Select a sim first", Toast.LENGTH_SHORT).show();
            return false;
        }
        // find the radiobutton by returned id
        rdSimButton = v.findViewById(selectedId);


        if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("mtn")) {
            SimNetwork = "Mtn";
            return true;
        } else if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("air")) {
            SimNetwork = "Airtel";
            return true;
        } else if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("glo")) {
            SimNetwork = "Glo";
            return true;
        } else if (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("9mo") ||
                (rdSimButton.getText().toString().substring(0, 3).equalsIgnoreCase("eti"))) {
            SimNetwork = "9mobile";
            return true;
        } else {
//            Toast.makeText(getContext(), "Selected Sim Not Recognized", Toast.LENGTH_LONG).show();
//            return false;
            SimNetwork = "9mobile";
            return true;
        }
    }
}
