package tingtel.payment.fragments.signup;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.chaos.view.PinView;

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
public class SignUpSim1OtpFragment extends Fragment {


    private Button btnConfirmOtp;
    private NavController navController;
    private String Sim1Network;
    private String Sim1Serial;
    private TextView resendOtp;
    private PinView pinView;
    private String Sim1PhoneNumber;
    private SessionManager sessionManager;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_sim1_otp, container, false);

        initViews(view);
        initListeners();
        getCarrierOfSim(getContext(), getActivity());

        return view;
    }

    private void initListeners() {
        btnConfirmOtp.setOnClickListener(v -> {
            String customerOTP = Objects.requireNonNull(pinView.getText()).toString().trim();
            String appOTP = sessionManager.getOTP();
//check if both otp's match
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
        resendOtp.setOnClickListener(v -> resendOTPtoCustomer());
    }

    private void resendOTPtoCustomer() {
        AppUtils.initLoadingDialog(getContext());

        SendOTPsendObject sendOTPsendObject = new SendOTPsendObject();
        sendOTPsendObject.setPhoneNumber(Sim1PhoneNumber);
        sendOTPsendObject.setNetwork(sessionManager.getUserNetwork());
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


    private void performProcessAction() {
        AppDatabase appdatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getContext()));
        if (appdatabase.simCardsDao().getSerial(Sim1Serial).size() > 0) {
            AppUtils.showDialog("This Sim has already been registered, kindly delete from setting and Re-register", getActivity());
        } else {
            saveSimDetails();
        }

        if (sessionManager.getIsRegistered()) {


            makeAddSimRequest();

        } else {
            String NoOfSIm = sessionManager.getSimStatus();

            switch (NoOfSIm) {
                case "SIM1":
                    navController.navigate(R.id.action_signUpSim1OtpFragment_to_setPasswordFragment, null);
                    break;
                case "SIM1 SIM2":
                    navController.navigate(R.id.action_signUpSim1OtpFragment_to_SIgnUpSim1SuccessFragment, null);
                    break;
            }
        }
    }

    private void saveSimDetails() {
        //serial and network name gotten were already set by user so lets set the phone number & network name (ported numbers)
        sessionManager.setSimPhoneNumber(Sim1PhoneNumber);
        sessionManager.setNetworkName(Sim1Network);

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                Date queryDate = Calendar.getInstance().getTime();
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getContext()));

                //creating a task
                SimCards simCards = new SimCards();

                simCards.setSimNetwork(Sim1Network);
                simCards.setSimSerial(Sim1Serial);
                simCards.setPhoneNumber(Sim1PhoneNumber);

                //adding to database
                appdatabase.simCardsDao().insert(simCards);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //Toast.makeText(getContext(), "Sim 1 Details Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    private void initViews(View view) {
        resendOtp = view.findViewById(R.id.resendOTPTextview);
        pinView = view.findViewById(R.id.pinView);
        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        btnConfirmOtp = view.findViewById(R.id.btn_confirm_otp);

        Sim1Network = Objects.requireNonNull(getArguments()).getString("Sim1Network");
        Sim1Serial = getArguments().getString("Sim1Serial");
        Sim1PhoneNumber = getArguments().getString("Sim1PhoneNumber");

        sessionManager = AppUtils.getSessionManagerInstance();

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }



    private void makeAddSimRequest() {
        AppUtils.initLoadingDialog(getContext());

        AddSimSendObject addSimSendObject = new AddSimSendObject();
        addSimSendObject.setEmail(sessionManager.getEmailFromLogin());
        addSimSendObject.setPhone2(Sim1PhoneNumber);
        addSimSendObject.setUser_phone(sessionManager.getNumberFromLogin());
        addSimSendObject.setSim2_network(Sim1Network);
        addSimSendObject.setSim2_serial(Sim1Serial);
        addSimSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

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
