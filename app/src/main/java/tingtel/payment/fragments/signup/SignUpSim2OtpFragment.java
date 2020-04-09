package tingtel.payment.fragments.signup;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.chaos.view.PinView;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;
import tingtel.payment.models.add_sim.AddSimResponse;
import tingtel.payment.models.add_sim.AddSimSendObject;
import tingtel.payment.models.otp.SendOTPresponse;
import tingtel.payment.models.otp.SendOTPsendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.AddSimInterface;
import tingtel.payment.web_services.interfaces.SendOTPinterface;

import static tingtel.payment.utils.NetworkCarrierUtils.getCarrierOfSim;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpSim2OtpFragment extends Fragment {
    private Button btnConfirmOtp;
    private NavController navController;
    private String Sim2Network;
    private TextView resendOTP;
    private String Sim2Serial;
    private String Sim2PhoneNumber;
    private PinView pinView;
    private SessionManager sessionManager;


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_sim2_otp, container, false);

        initViews(view);
        initListeners();
        getCarrierOfSim(getContext(), getActivity());
        getExtrasFromIntent();

        return view;
    }

    private void initListeners() {
        btnConfirmOtp.setOnClickListener(v -> {
            String customerOTP = Objects.requireNonNull(pinView.getText()).toString().trim();
            String appOTP = sessionManager.getOTP();

            if (customerOTP.equals(appOTP)) {
                Toast.makeText(getContext(), "Verified", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(this::performProcessAction, 1500);
            } else {
                AppUtils.showSnackBar("Incorrect OTP, please try again", getView());
                pinView.setText(null);
                pinView.requestFocus();
                Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        });
        resendOTP.setOnClickListener(v -> resendOTPtoCustomer());
    }

    private void performProcessAction() {
        AppDatabase appdatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getContext()));
        if (appdatabase.simCardsDao().getSerial(Sim2Serial).size() > 0) {
            Toast.makeText(getActivity(), "This Sim has already been registered, kindly delete from setting and Re-register", Toast.LENGTH_LONG).show();
        } else {
            saveSimDetails();
        }

        if (sessionManager.getIsRegistered()) {
            if (AppUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                makeAddSimRequest();
            } else {
                AppUtils.showSnackBar("No network available", resendOTP);
            }
        } else {
            navController.navigate(R.id.action_signUpSim2OtpFragment_to_setPasswordFragment, null);
        }
    }


    private void resendOTPtoCustomer() {
        AppUtils.initLoadingDialog(getContext());

        SendOTPsendObject sendOTPsendObject = new SendOTPsendObject();
        sendOTPsendObject.setPhoneNumber(Sim2PhoneNumber);
        sendOTPsendObject.setNetwork(sessionManager.getUserNetwork1());
        sendOTPsendObject.setOtp(sessionManager.getOTP());
        sendOTPsendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.sendOTPtoCustomer(sendOTPsendObject, new SendOTPinterface() {
            @Override
            public void onSuccess(SendOTPresponse sendOTPresponse) {
                AppUtils.dismissLoadingDialog();
                AppUtils.showSnackBar("Code resent", getView());

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

    private void saveSimDetails() {

        //serial and network name gotten were already set by user so lets set the phone number & network name (ported numbers)
        sessionManager.setSimPhoneNumber1(Sim2PhoneNumber);
        sessionManager.setNetworkName1(Sim2Network);

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Date queryDate = Calendar.getInstance().getTime();
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getContext()));

                //creating a task
                SimCards simCards = new SimCards();

                simCards.setSimNetwork(Sim2Network);
                simCards.setSimSerial(Sim2Serial);
                simCards.setPhoneNumber(Sim2PhoneNumber);

                //adding to database
                appdatabase.simCardsDao().insert(simCards);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //Toast.makeText(getContext(), "Sim 2 Details Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }


    private void initViews(View view) {
        pinView = view.findViewById(R.id.pinView);
        resendOTP = view.findViewById(R.id.resendOTPTextview);
        btnConfirmOtp = view.findViewById(R.id.btn_confirm_otp);

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
        sessionManager = AppUtils.getSessionManagerInstance();
    }

    private void getExtrasFromIntent() {
        Sim2Network = Objects.requireNonNull(getArguments()).getString("Sim2Network");
        Sim2Serial = getArguments().getString("Sim2Serial");
        Sim2PhoneNumber = getArguments().getString("Sim2PhoneNumber");
    }

    private void makeAddSimRequest() {
        AppUtils.initLoadingDialog(getContext());

        AddSimSendObject addSimSendObject = new AddSimSendObject();
        addSimSendObject.setEmail(sessionManager.getEmailFromLogin());
        addSimSendObject.setPhone2(Sim2PhoneNumber);
        addSimSendObject.setUserPhone(sessionManager.getNumberFromLogin());
        addSimSendObject.setSim2Network(Sim2Network);
        addSimSendObject.setSim2Serial(Sim2Serial);
        addSimSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(addSimSendObject);
        sessionManager.setAddSimJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.addSim(addSimSendObject, new AddSimInterface() {
            @Override
            public void onSuccess(AddSimResponse addSimResponse) {
                AppUtils.dismissLoadingDialog();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity(), "Sim added successfully", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                Toast.makeText(getActivity(), String.valueOf(errorCode), Toast.LENGTH_SHORT).show();
                AppUtils.dismissLoadingDialog();
            }
        });
    }
}
