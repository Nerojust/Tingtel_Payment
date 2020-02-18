package tingtel.payment.fragments.signup;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import tingtel.payment.R;
import tingtel.payment.adapters.SpinnerAdapter;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.NetworkCarrierUtils.getCarrierOfSim;

public class SignUpSim2Fragment extends Fragment {

    Spinner  mSpinner;
    String[] spinnerTitles;
    String selectedSpinnerNetwork;
    int[] spinnerImages;
    Button btnNext;
    NavController navController;
    TextInputEditText tvPhoneNumber;
    SessionManager sessionManager;
    String Sim2Network;
    String  Sim2Serial;
    String NoOfSIm;
    TextView tvSimInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;

        view = inflater.inflate(R.layout.fragment_sign_up_sim2, container, false);

        initViews(view);
        initListeners(view);
        getCarrierOfSim(getContext(), getActivity());
        getDataFromCarrier(view);


        spinnerTitles = new String[]{"Mtn", "Airtel", "9Mobile", "Glo"};
        spinnerImages = new int[]{R.drawable.mtn_logo
                , R.drawable.airtel_logo, R.drawable.nmobile_logo, R.drawable.glo_logo
        };

        SpinnerAdapter mCustomAdapter = new SpinnerAdapter (getActivity(), spinnerTitles, spinnerImages);
        mSpinner.setAdapter(mCustomAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getActivity(), spinnerTitles[i], Toast.LENGTH_SHORT).show();
                selectedSpinnerNetwork = spinnerTitles[i];

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    private void initListeners(View view) {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvPhoneNumber.getText().toString().equalsIgnoreCase("")) {
                    //TODO: correct phone number validation
                    tvPhoneNumber.setError("Kindly Input a valid phone number");
                    tvPhoneNumber.setFocusable(true);
                    return;
                }



                Bundle bundle = new Bundle();
                bundle.putString("Sim2Serial", Sim2Serial);
                bundle.putString("Sim2Network", selectedSpinnerNetwork);
                bundle.putString("Sim2PhoneNumber", tvPhoneNumber.getText().toString());
                navController.navigate(R.id.action_signUpSim2Fragment_to_signUpSim2OtpFragment2, bundle);
            }
        });
    }

    private void initViews(View view) {
        btnNext = view.findViewById(R.id.btn_next);
        tvPhoneNumber = view.findViewById(R.id.tv_phone_number);
        mSpinner = (Spinner) view.findViewById(R.id.sp_network);

        tvSimInfo = view.findViewById(R.id.tv_info);
        sessionManager = AppUtils.getSessionManagerInstance();

        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(navhost);
    }


    private void getDataFromCarrier(View view) {



        Sim2Network = sessionManager.getNetworkName1();
        Sim2Serial = sessionManager.getSimSerialICCID1();


        NoOfSIm = sessionManager.getSimStatus();
        Log.e("getDefaultCarrier", "No of sim is " + NoOfSIm);


        if (Sim2Network.substring(0, 3).equalsIgnoreCase("mtn")) {

            mSpinner.setSelection(0);
            tvSimInfo.setText("Mtn Sim Detected");

        } else if (Sim2Network.substring(0, 3).equalsIgnoreCase("air")) {

            mSpinner.setSelection(1);
            tvSimInfo.setText("Airtel Sim Detected");

        } else if (Sim2Network.substring(0, 3).equalsIgnoreCase("9mo") ||
                Sim2Network.substring(0, 3).equalsIgnoreCase("eti")) {

            mSpinner.setSelection(2);
            tvSimInfo.setText("9Mobile Sim Detected");

        } else if (Sim2Network.substring(0, 3).equalsIgnoreCase("glo")) {

            mSpinner.setSelection(3);
            tvSimInfo.setText("Airtel Sim Detected");

        }



    }


}
