package tingtel.payment.fragments.signup;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.adapters.SpinnerAdapter;
import tingtel.payment.models.otp.SendOTPresponse;
import tingtel.payment.models.otp.SendOTPsendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.SendOTPinterface;

import static tingtel.payment.utils.NetworkCarrierUtils.getCarrierOfSim;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpSim1Fragment extends Fragment {

    private Spinner mSpinner;
    private String[] spinnerTitles;
    private String selectedSpinnerNetwork;
    private String[] spinnerPopulation;
    private NavController navController;
    private Button btnSaveSim1NetworkDetails;
    private TextInputEditText tvPhoneNumber;
    private SessionManager sessionManager;
    private String Sim1Network, Sim2Network;
    private String Sim1Serial, Sim2Serial;
    private String NoOfSIm;
    private TextView tvSimInfo;
    private String phoneNumber;
    private String generatedOTP;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
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
        int[] spinnerImages = new int[]{R.drawable.mtn_logo, R.drawable.airtel_logo, R.drawable.nmobile_logo, R.drawable.glo_logo};

        SpinnerAdapter mCustomAdapter = new SpinnerAdapter(Objects.requireNonNull(getActivity()), spinnerTitles, spinnerImages);
        mSpinner.setAdapter(mCustomAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSpinnerNetwork = spinnerTitles[i];
                sessionManager.setUserNetwork(selectedSpinnerNetwork);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initListeners(View view) {
        btnSaveSim1NetworkDetails.setOnClickListener(v -> {
            if (isValidFields()) {
                generatedOTP = AppUtils.generateOTP();
                sessionManager.setOTP(generatedOTP);
                phoneNumber = AppUtils.checkPhoneNumberAndRestructure(Objects.requireNonNull(tvPhoneNumber.getText()).toString().trim());
                //Toast.makeText(getContext(), phoneNumber, Toast.LENGTH_SHORT).show();
                if (AppUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                    sendOTPtoCustomer();
                } else {
                    AppUtils.showSnackBar("No network available", tvPhoneNumber);
                }

            }
        });
    }

    private void sendOTPtoCustomer() {
        AppUtils.initLoadingDialog(getContext());

        SendOTPsendObject sendOTPsendObject = new SendOTPsendObject();
        sendOTPsendObject.setPhoneNumber(phoneNumber);
        sendOTPsendObject.setNetwork(selectedSpinnerNetwork);
        sendOTPsendObject.setOtp(generatedOTP);
        sendOTPsendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(sendOTPsendObject);
        sessionManager.setOTPJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.sendOTPtoCustomer(sendOTPsendObject, new SendOTPinterface() {
            @Override
            public void onSuccess(SendOTPresponse sendOTPresponse) {
                AppUtils.dismissLoadingDialog();

                Bundle bundle = getBundle();
                navController.navigate(R.id.action_signUpSim1Fragment_to_signUpSim1OtpFragment, bundle);
            }

            @Override
            public void onError(String error) {
                AppUtils.showDialog(error, getActivity());

                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showSnackBar(String.valueOf(errorCode), getView());
                AppUtils.dismissLoadingDialog();
            }
        });
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("Sim1Serial", Sim1Serial);
//                bundle.putString("Sim1Network", Sim1Network);
        bundle.putString("Sim1Network", selectedSpinnerNetwork);
        bundle.putString("Sim1PhoneNumber", Objects.requireNonNull(Objects.requireNonNull(phoneNumber)));
        return bundle;
    }

    private boolean isValidFields() {
        if (Objects.requireNonNull(tvPhoneNumber.getText()).toString().isEmpty()) {
            AppUtils.showSnackBar("Number is required", tvPhoneNumber);
            tvPhoneNumber.requestFocus();
            return false;
        }
        if (tvPhoneNumber.getText().toString().length() < 11) {
            AppUtils.showSnackBar("Number is too short", tvPhoneNumber);
            tvPhoneNumber.requestFocus();
            return false;
        }

        return true;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRemoving()) {
            if (sessionManager.getComingFromDashboard()) {
                startActivity(new Intent(getContext(), MainActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        }
    }
}
