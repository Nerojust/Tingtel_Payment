package tingtel.payment.fragments.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import tingtel.payment.MainActivity;
import tingtel.payment.R;
import tingtel.payment.SignUpActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.AppUtils.dialUssdCode;


public class TransferAirtimeFragment extends Fragment {

    Button btnCheckBalance;
    private RadioGroup rdSimGroup;
    private RadioButton rdSimButton;
    private RadioButton rdSim1, rdSim2;
    EditText edAmount;
    Button btnNext;
    NavController navController;
    String UssdCode;
    String SimNetwork;
    int SimNo;
    String SimSerial;
    SessionManager sessionManager;
    AppDatabase appDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_airtime, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Transfer Airtime");

        sessionManager = AppUtils.getSessionManagerInstance();

        rdSimGroup = (RadioGroup) view.findViewById(R.id.radioSim);
        btnCheckBalance = (Button) view.findViewById(R.id.btn_check_balance);
        btnNext = (Button) view.findViewById(R.id.btn_next);
        edAmount = (EditText) view.findViewById(R.id.ed_amount);

        rdSim1 = (RadioButton) view.findViewById(R.id.radioSim1);
        rdSim2 = (RadioButton) view.findViewById(R.id.radioSim2);

        appDatabase = AppDatabase.getDatabaseInstance(getContext());

        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(navhost);



        confirmSimRegistrations();








        populateSimRadioButtons();


        rdSimGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                //  Toast.makeText(getContext(), "testing for checked", Toast.LENGTH_LONG).show();
                // get selected radio button from radioGroup
                int selectedId = rdSimGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rdSimButton = (RadioButton) view.findViewById(selectedId);

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

            }
        });


        btnCheckBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // get selected radio button from radioGroup
                int selectedId = rdSimGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rdSimButton = (RadioButton) view.findViewById(selectedId);


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
                    Toast.makeText(getActivity(), "Cant Check Ussd Balance", Toast.LENGTH_LONG).show();
                    return;
                }

                dialUssdCode(getActivity(), UssdCode, SimNo);

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkSelectedSim(view)) {
                    return;
                }
                if (!validateFields(view)) {
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("simNetwork", SimNetwork);
                bundle.putString("simSerial", "" + SimSerial);
                bundle.putInt("simNo", SimNo);
                bundle.putString("amount", edAmount.getText().toString());
                navController.navigate(R.id.action_transferAirtimeFragment_to_transferAirtimeReceiverInfoFragment, bundle);


            }
        });

        return view;
    }

    private void confirmSimRegistrations() {
        if (!sim1ExistsCheck()) {
            Toast.makeText(getActivity(), "New Sim Detected, You Need to Register this sim on your account", Toast.LENGTH_LONG).show();
            // navigateToSim1Register();
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            intent.putExtra("task", "registerSim1");
            startActivity(intent);
            return;
        }

        if (!sim2ExistsCheck()) {
            Toast.makeText(getActivity(), "New Sim Detected, You Need to Register this sim on your account", Toast.LENGTH_LONG).show();
            // navigateToSim2Register();
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            intent.putExtra("task", "registerSim2");
            startActivity(intent);
            return;
        }


    }


    private boolean sim1ExistsCheck() {

        String Sim1Serial = sessionManager.getSimSerialICCID();

        if (appDatabase.simCardsDao().getSerial(Sim1Serial).size() > 0) {

            return true;
        } else {
            return false;
        }

    }

    private boolean sim2ExistsCheck() {

        String Sim2Serial = sessionManager.getSimSerialICCID1();

        if (appDatabase.simCardsDao().getSerial(Sim2Serial).size() > 0) {

            return true;
        } else {
            return false;
        }

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

                rdSim1.setText(sessionManager.getNetworkName() + " ("+ sessionManager.getSimPhoneNumber()+")");
                rdSim2.setText(sessionManager.getNetworkName1() + " ("+ sessionManager.getSimPhoneNumber1()+")");
                break;
        }


    }

    private boolean validateFields(View view) {

        if (edAmount.getText().toString().equalsIgnoreCase("")) {
            edAmount.setError("Kindly Input an Amount");
            edAmount.setFocusable(true);
            return false;
        } else {
            return true;
        }


    }

    private boolean checkSelectedSim(View v) {


        int selectedId = rdSimGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        rdSimButton = (RadioButton) v.findViewById(selectedId);

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
            Toast.makeText(getContext(), "Selected Sim Not Recognised", Toast.LENGTH_LONG).show();
            return false;
        }
    }


}
