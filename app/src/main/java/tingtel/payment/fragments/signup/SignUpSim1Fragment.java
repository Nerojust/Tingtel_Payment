package tingtel.payment.fragments.signup;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.adapters.SpinnerAdapter;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.NetworkCarrierUtils.getCarrierOfSim;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpSim1Fragment extends Fragment {

    private Spinner mSpinner;
    private String[] spinnerTitles;
    private String selectedSpinnerNetwork;
    private String[] spinnerPopulation;
    private int[] spinnerImages;
    private NavController navController;
    private Button btnSaveSim1NetworkDetails;
    private TextInputEditText tvPhoneNumber;
    private SessionManager sessionManager;
    private String Sim1Network, Sim2Network;
    private String Sim1Serial, Sim2Serial;
    private String NoOfSIm;
    private TextView tvSimInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_sim1, container, false);

        initViews(view);
        initListeners(view);
        getCarrierOfSim(getContext(), getActivity());
        getDataFromCarrier(view);
        initSpinner();

        return view;
    }

    private void initSpinner() {
        spinnerTitles = new String[]{"Mtn", "Airtel", "9Mobile", "Glo"};
        spinnerImages = new int[]{R.drawable.mtn_logo, R.drawable.airtel_logo, R.drawable.nmobile_logo, R.drawable.glo_logo};

        SpinnerAdapter mCustomAdapter = new SpinnerAdapter(Objects.requireNonNull(getActivity()), spinnerTitles, spinnerImages);
        mSpinner.setAdapter(mCustomAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Toast.makeText(getActivity(), spinnerTitles[i], Toast.LENGTH_SHORT).show();
                selectedSpinnerNetwork = spinnerTitles[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initListeners(View view) {
        btnSaveSim1NetworkDetails.setOnClickListener(v -> {
            //todo: add validations

            if (Objects.requireNonNull(tvPhoneNumber.getText()).toString().equalsIgnoreCase("")) {
                //TODO: correct phone number validation
                tvPhoneNumber.setError("Kindly Input a valid phone number");
                tvPhoneNumber.setFocusable(true);
                return;
            }
            sessionManager.setSenderPhoneNumber(tvPhoneNumber.getText().toString());
            Bundle bundle = new Bundle();
            bundle.putString("Sim1Serial", Sim1Serial);
//                bundle.putString("Sim1Network", Sim1Network);
            bundle.putString("Sim1Network", selectedSpinnerNetwork);
            bundle.putString("Sim1PhoneNumber", tvPhoneNumber.getText().toString());
            navController.navigate(R.id.action_signUpSim1Fragment_to_signUpSim1OtpFragment, bundle);
        });
    }

    private void initViews(View view) {
        btnSaveSim1NetworkDetails = view.findViewById(R.id.btn_next);
        tvPhoneNumber = view.findViewById(R.id.tv_phone_number);
        tvSimInfo = view.findViewById(R.id.tv_sim_info);
        mSpinner = view.findViewById(R.id.sp_network);
        sessionManager = AppUtils.getSessionManagerInstance();

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }


    private void getDataFromCarrier(View view) {
        Sim1Network = sessionManager.getNetworkName();
        Sim2Network = sessionManager.getNetworkName1();

        Sim1Serial = sessionManager.getSimSerialICCID();
        Sim2Serial = sessionManager.getSimSerialICCID1();

        NoOfSIm = sessionManager.getSimStatus();
        Log.e("getDefaultCarrier", "No of sim is " + NoOfSIm);

        if (Sim1Network.substring(0, 3).equalsIgnoreCase("mtn")) {
            mSpinner.setSelection(0);
            tvSimInfo.setText(getResources().getString(R.string.mtn_sim_detected));

        } else if (Sim1Network.substring(0, 3).equalsIgnoreCase("air")) {
            mSpinner.setSelection(1);
            tvSimInfo.setText(getResources().getString(R.string.airtel_sim_detected));

        } else if (Sim1Network.substring(0, 3).equalsIgnoreCase("9mo") ||
                Sim1Network.substring(0, 3).equalsIgnoreCase("eti")) {
            mSpinner.setSelection(2);
            tvSimInfo.setText(getResources().getString(R.string.nine_mobile_sim_detected));

        } else if (Sim1Network.substring(0, 3).equalsIgnoreCase("glo")) {
            mSpinner.setSelection(3);
            tvSimInfo.setText(getResources().getString(R.string.glo_sim_detected));
        }
    }
}
