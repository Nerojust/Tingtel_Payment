package tingtel.payment.activities.forgot_password;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.models.otp.SendOTPresponse;
import tingtel.payment.models.otp.SendOTPsendObject;
import tingtel.payment.models.validate_user.ValidateUserResponse;
import tingtel.payment.models.validate_user.ValidateUserSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.SendOTPinterface;
import tingtel.payment.web_services.interfaces.ValidateUserInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordHomeFragment extends Fragment {
    Button btnGetOtp;
    EditText edInput;
    SessionManager sessionManager;
    private NavController navController;
    private String generatedOTP;
    private String phoneNumberFromServer;
    private String networkFromServer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password_home, container, false);
        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_forgot_password_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));

        initViews(view);
        initListeners();

        return view;
    }

    private void initListeners() {

        btnGetOtp.setOnClickListener(v -> {

            if (AppUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                if (isValidFields()) {
                    validateUserCall();
                }

            } else {
                AppUtils.showSnackBar("No network available", edInput);
            }


            //  navController.navigate(R.id.action_forgotPasswordHomeFragment_to_forgotPasswordOtpFragment);
        });
    }

    private void validateUserCall() {
        AppUtils.initLoadingDialog(getContext());

        ValidateUserSendObject validateUserSendObject = new ValidateUserSendObject();
        validateUserSendObject.setTransactionValue(edInput.getText().toString());
        validateUserSendObject.setHash(AppUtils.getSHA512("tingtel" + BuildConfig.HEADER_PASSWORD + BuildConfig.HASH_KEY));


        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.validateUser(validateUserSendObject, new ValidateUserInterface() {
            @Override
            public void onSuccess(ValidateUserResponse validateUserResponse) {
                generatedOTP = AppUtils.generateOTP();
                phoneNumberFromServer = validateUserResponse.getData().getPhone();
                networkFromServer = validateUserResponse.getData().getUserNetwork();
                sessionManager.setOTP(generatedOTP);

                sendOTPtoCustomer();
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onErrorCode(int errorCode) {

            }
        });
    }

    private void initViews(View view) {
        btnGetOtp = view.findViewById(R.id.btn_get_otp);
        edInput = view.findViewById(R.id.ed_input);
        Fragment navHost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_forgot_password_fragment);
        navController = NavHostFragment.findNavController(navHost);
        sessionManager = AppUtils.getSessionManagerInstance();
    }

    private boolean isValidFields() {
        if (Objects.requireNonNull(edInput.getText()).toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.this_is_required), edInput);
            edInput.requestFocus();
            return false;
        }


        return true;
    }

    private void sendOTPtoCustomer() {
        AppUtils.initLoadingDialog(getContext());

        SendOTPsendObject sendOTPSendObject = new SendOTPsendObject();
        sendOTPSendObject.setPhoneNumber(phoneNumberFromServer);
        sendOTPSendObject.setNetwork(networkFromServer);
        sendOTPSendObject.setOtp(generatedOTP);
        sendOTPSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.sendOTPtoCustomer(sendOTPSendObject, new SendOTPinterface() {
            @Override
            public void onSuccess(SendOTPresponse sendOTPresponse) {
                Bundle bundle = new Bundle();
                bundle.putString("SimNetwork", networkFromServer);
                bundle.putString("SimPhoneNumber", phoneNumberFromServer);
                navController.navigate(R.id.action_forgotPasswordHomeFragment_to_forgotPasswordOtpFragment, bundle);
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
