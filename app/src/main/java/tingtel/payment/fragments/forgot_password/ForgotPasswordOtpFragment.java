package tingtel.payment.fragments.forgot_password;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.chaos.view.PinView;

import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.models.otp.SendOTPresponse;
import tingtel.payment.models.otp.SendOTPsendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.SendOTPinterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordOtpFragment extends Fragment {

    private Button btnConfirmOtp;
    private TextView tvPhoneNumber;
    private PinView pinView;
    private TextView tvResendOtp;
    private SessionManager sessionManager;
    private String phoneNumber;
    private String network;

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password_otp, container, false);
        Fragment navhost = requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_forgot_password_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
        initViews(view);
        initListeners();
        getDataFromExtras();
        displayPhoneNumber();

        return view;
    }

    private void displayPhoneNumber() {
        String shortPhoneNumber = phoneNumber.substring(phoneNumber.length() - 4, phoneNumber.length());
        tvPhoneNumber.setText("*******" + shortPhoneNumber);

    }

    private void getDataFromExtras() {
        phoneNumber = getArguments().getString("SimPhoneNumber");
        network = getArguments().getString("SimNetwork");
    }

    private void initListeners() {
        tvResendOtp.setOnClickListener(v -> resendOTPtoCustomer());
        btnConfirmOtp.setOnClickListener(v -> {
            String customerOTP = Objects.requireNonNull(pinView.getText()).toString().trim();
            String appOTP = sessionManager.getOTP();
            //check if both otp's match
            if (customerOTP.equals(appOTP)) {
                Toast.makeText(getContext(), "Verified", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(this::performProcessAction, 2000);

            } else {
                AppUtils.showSnackBar("Incorrect OTP, please try again", getView());
                pinView.setText(null);
                pinView.requestFocus();
            }
        });
    }

    private void performProcessAction() {
        Bundle bundle = new Bundle();
        bundle.putString("phone", phoneNumber);
        navController.navigate(R.id.action_forgotPasswordOtpFragment_to_forgotPassword_Set_PasswordFragment, bundle);
    }

    private void initViews(View view) {
        sessionManager = AppUtils.getSessionManagerInstance();

        tvPhoneNumber = view.findViewById(R.id.tv_phone_number);
        btnConfirmOtp = view.findViewById(R.id.btn_confirm_otp);
        pinView = view.findViewById(R.id.pinView);
        tvResendOtp = view.findViewById(R.id.resendOTPTextview);

    }

    private void resendOTPtoCustomer() {
        SendOTPsendObject sendOTPSendObject = new SendOTPsendObject();
        sendOTPSendObject.setPhoneNumber(phoneNumber);
        sendOTPSendObject.setNetwork(network);
        sendOTPSendObject.setOtp(sessionManager.getOTP());
        sendOTPSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.sendOTPtoCustomer(sendOTPSendObject, new SendOTPinterface() {
            @Override
            public void onSuccess(SendOTPresponse sendOTPresponse) {
                AppUtils.showSnackBar("Code resent", getView());
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onError(String error) {
                AppUtils.showDialog(error, getActivity());
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.dismissLoadingDialog();
                AppUtils.showDialog(String.valueOf(errorCode), getActivity());
            }
        });
    }
}
