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

import static tingtel.payment.utils.AppUtils.dialUssdCode;


public class TransferAirtimeFragment extends Fragment {

    Button btnCheckBalance;
    private RadioGroup rdSimGroup;
    private RadioButton rdSimButton;
    EditText edAmount;
    Button btnNext;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_transfer_airtime, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Transfer Airtime");



        rdSimGroup = (RadioGroup) view.findViewById(R.id.radioSim);
        btnCheckBalance = (Button) view.findViewById(R.id.btn_check_balance);
        btnNext = (Button) view.findViewById(R.id.btn_next);
        edAmount = (EditText) view.findViewById(R.id.ed_amount);





        btnCheckBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // get selected radio button from radioGroup
                int selectedId = rdSimGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rdSimButton = (RadioButton) view.findViewById(selectedId);

                String UssdCode;
                int SimNo;

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

                switch(selectedId){
                    case R.id.radioSim1:
                       SimNo = 0;
                        break;
                    case R.id.radioSim2:
                       SimNo = 1;
                        break;
                   default:
                       return;
                }

                dialUssdCode(getActivity(), UssdCode, SimNo);

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }




}
