package tingtel.payment.fragments.signup;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

public class SignUpSim2Fragment extends Fragment {

    private Spinner mSpinner;
    private String[] spinnerTitles;
    private String selectedSpinnerNetwork;
    private int[] spinnerImages;
    private Button btnNext;
    private NavController navController;
    private TextInputEditText tvPhoneNumber;
    private SessionManager sessionManager;
    private String Sim2Network;
    private String Sim2Serial;
    private String NoOfSIm;
    private TextView tvSimInfo;
    private String generatedOTP;
    private String phoneNumber;


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_sim2, container, false);

        initViews(view);
        initListeners();
        getCarrierOfSim(getContext(), getActivity());
        getDataFromCarrier();
        initSpinner();

        return view;
    }

    private void initSpinner() {
        spinnerTitles = new String[]{"Mtn", "Airtel", "9Mobile", "Glo"};
        spinnerImages = new int[]{R.drawable.mtn_logo, R.drawable.airtellogo, R.drawable.nmobile_logo, R.drawable.glo_logo};

        SpinnerAdapter mCustomAdapter = new SpinnerAdapter(Objects.requireNonNull(getActivity()), spinnerTitles, spinnerImages);
        mSpinner.setAdapter(mCustomAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSpinnerNetwork = spinnerTitles[i];
                sessionManager.setUserNetwork1(selectedSpinnerNetwork);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean isValidFields() {
        if (Objects.requireNonNull(tvPhoneNumber.getText()).toString().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.this_is_required), tvPhoneNumber);
            tvPhoneNumber.requestFocus();
            return false;
        }
        if (tvPhoneNumber.getText().toString().length() < 11) {
            AppUtils.showSnackBar(getResources().getString(R.string.number_too_short), tvPhoneNumber);
            tvPhoneNumber.requestFocus();
            return false;
        }

        return true;
    }

    private void initListeners() {
        btnNext.setOnClickListener(v -> {
//todo: add validations
            if (isValidFields()) {
                generatedOTP = AppUtils.generateOTP();
                sessionManager.setOTP(generatedOTP);

                phoneNumber = AppUtils.checkPhoneNumberAndRestructure(Objects.requireNonNull(tvPhoneNumber.getText()).toString().trim());

                if (AppUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                    sendOTPtoCustomer();
                } else {
                    AppUtils.showSnackBar(getResources().getString(R.string.no_network_available), tvPhoneNumber);
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

                navController.navigate(R.id.action_signUpSim2Fragment_to_signUpSim2OtpFragment2, bundle);

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
        bundle.putString("Sim2Serial", Sim2Serial);
        bundle.putString("Sim2Network", selectedSpinnerNetwork);
        bundle.putString("Sim2PhoneNumber", Objects.requireNonNull(phoneNumber));
        return bundle;
    }

    private void initViews(View view) {
        btnNext = view.findViewById(R.id.btn_next);
        tvPhoneNumber = view.findViewById(R.id.tv_phone_number);
        mSpinner = view.findViewById(R.id.sp_network);

        tvSimInfo = view.findViewById(R.id.tv_sim_info);
        sessionManager = AppUtils.getSessionManagerInstance();

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }


    private void getDataFromCarrier() {
        Sim2Network = sessionManager.getSimTwoNetworkName();
        Sim2Serial = sessionManager.getSimSerialICCID1();

        NoOfSIm = sessionManager.getSimStatus();
        //Log.e("getDefaultCarrier", "No of sim is " + NoOfSIm);

        if (Sim2Network.substring(0, 3).equalsIgnoreCase("mtn")) {
            mSpinner.setSelection(0);
            tvSimInfo.setText(getResources().getString(R.string.mtn_sim_detected));

        } else if (Sim2Network.substring(0, 3).equalsIgnoreCase("air")) {
            mSpinner.setSelection(1);
            tvSimInfo.setText(getResources().getString(R.string.airtel_sim_detected));

        } else if (Sim2Network.substring(0, 3).equalsIgnoreCase("9mo") ||
                Sim2Network.substring(0, 3).equalsIgnoreCase("eti")) {
            mSpinner.setSelection(2);
            tvSimInfo.setText(getResources().getString(R.string.nine_mobile_sim_detected));

        } else if (Sim2Network.substring(0, 3).equalsIgnoreCase("glo")) {
            mSpinner.setSelection(3);
            tvSimInfo.setText(getResources().getString(R.string.glo_sim_detected));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

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
