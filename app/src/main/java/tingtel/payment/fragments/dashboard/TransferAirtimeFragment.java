package tingtel.payment.fragments.dashboard;

import android.Manifest;
import android.content.Context;
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
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.AppUtils.dialUssdCode;


public class TransferAirtimeFragment extends Fragment {

    Button btnCheckBalance;
    private RadioGroup rdSimGroup;
    private RadioButton rdSimButton;
    EditText edAmount;
    Button btnNext;
    NavController navController;
    String UssdCode;
    String SimNetwork;
    int SimNo;
    String SimSerial;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_transfer_airtime, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Transfer Airtime");

sessionManager = AppUtils.getSessionManagerInstance();

        rdSimGroup = (RadioGroup) view.findViewById(R.id.radioSim);
        btnCheckBalance = (Button) view.findViewById(R.id.btn_check_balance);
        btnNext = (Button) view.findViewById(R.id.btn_next);
        edAmount = (EditText) view.findViewById(R.id.ed_amount);
        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(navhost);




        rdSimGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // get selected radio button from radioGroup
                int selectedId = rdSimGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rdSimButton = (RadioButton) view.findViewById(selectedId);

                if (rdSimButton.getText().toString().substring(0,3).equalsIgnoreCase("mtn")) {
                    SimNetwork = "Mtn";
                } else if (rdSimButton.getText().toString().substring(0,3).equalsIgnoreCase("air")) {
                    SimNetwork = "Airtel";
                } else if (rdSimButton.getText().toString().substring(0,3).equalsIgnoreCase("glo")) {
                    SimNetwork = "Glo";
                } else if (rdSimButton.getText().toString().substring(0,3).equalsIgnoreCase("9mo") ||
                        (rdSimButton.getText().toString().substring(0,3).equalsIgnoreCase("eti"))) {
                    SimNetwork = "9mobile";
                } else {

                    return;
                }


                switch(selectedId){
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


                if (rdSimButton.getText().toString().substring(0,3).equalsIgnoreCase("mtn")) {
                UssdCode = "*556#";
                } else if (rdSimButton.getText().toString().substring(0,3).equalsIgnoreCase("air")) {
                    UssdCode = "*123#";
                } else if (rdSimButton.getText().toString().substring(0,3).equalsIgnoreCase("glo")) {
                    UssdCode = "*124*1#";
                } else if (rdSimButton.getText().toString().substring(0,3).equalsIgnoreCase("9mo") ||
                        (rdSimButton.getText().toString().substring(0,3).equalsIgnoreCase("eti"))) {
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

                Bundle bundle = new Bundle();
                bundle.putString("simNetwork", SimNetwork);
                bundle.putString("simSerial", SimSerial);
                bundle.putInt("simNo", SimNo);
                bundle.putString("amount", edAmount.getText().toString());
                navController.navigate(R.id.action_transferAirtimeFragment_to_transferAirtimeReceiverInfoFragment, bundle);


            }
        });

        return view;
    }




}
