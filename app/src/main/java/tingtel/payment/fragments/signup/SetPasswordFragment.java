package tingtel.payment.fragments.signup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.sign_in.SignInActivity;
import tingtel.payment.models.Registration.CustomerRegistrationResponse;
import tingtel.payment.models.Registration.CustomerRegistrationSendObject;
import tingtel.payment.models.Registration.Sim1;
import tingtel.payment.models.Registration.Sim2;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.Constants;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.CreateNewUserInterface;


public class SetPasswordFragment extends Fragment {

    private TextInputEditText tvPassword1;
    private TextInputEditText tvPassword2;
    private Button btnSetPassword;
    private SessionManager sessionManager;
    private String hash;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_password, container, false);

        initViews(view);
        initListeners(view);

        return view;
    }

    private void initListeners(View view) {
        btnSetPassword.setOnClickListener(v -> {
            if (isValidFields()) {
                registerUser();
            }
        });

    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Objects.requireNonNull(getActivity()).startActivity(intent);

        sessionManager.setIsRegistered(true);
        getActivity().finish();
    }

    private void registerUser() {
        AppUtils.initLoadingDialog(getContext());

        Sim1 sim1 = new Sim1();
        sim1.setPhone(sessionManager.getSimPhoneNumber());
        sim1.setUserNetwork(sessionManager.getUserNetwork());
        sim1.setSerialNumber(sessionManager.getSimSerialICCID());

        Sim2 sim2 = new Sim2();
        sim2.setPhone(sessionManager.getSimPhoneNumber1());
        sim2.setUserNetwork(sessionManager.getNetworkName1());
        sim2.setSerialNumber(sessionManager.getSimSerialICCID1());

        CustomerRegistrationSendObject customerRegistrationSendObject = new CustomerRegistrationSendObject();
        customerRegistrationSendObject.setFirstName(sessionManager.getFirstName());
        customerRegistrationSendObject.setLastName(sessionManager.getLastName());
        customerRegistrationSendObject.setEmail(sessionManager.getEmailAddress());
        customerRegistrationSendObject.setUsername(sessionManager.getUserame());
        customerRegistrationSendObject.setSim1(sim1);
        customerRegistrationSendObject.setSim2(sim2);
        customerRegistrationSendObject.setPassword(Objects.requireNonNull(tvPassword1.getText()).toString().trim());
        customerRegistrationSendObject.setHash(AppUtils.generateHash(sessionManager.getEmailAddress(),sessionManager.getSimPhoneNumber()));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(customerRegistrationSendObject);
        sessionManager.setRegistrationJsonObject(jsonObject);


        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.createANewUser(customerRegistrationSendObject, new CreateNewUserInterface() {
            @Override
            public void onSuccess(CustomerRegistrationResponse newUser) {
                AppUtils.dismissLoadingDialog();
                sessionManager.clearSharedPreferences();
                gotoLoginActivity();
                Toast.makeText(getContext(), "Please login in with new details", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                if (error.equalsIgnoreCase("invalid hash key")) {
                    AppUtils.showSnackBar("Server error. Please try again later.", tvPassword1);
                } else {
                    AppUtils.showSnackBar(error, tvPassword1);
                }
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                //AppUtils.showDialog(String.valueOf(errorCode), getActivity());
                AppUtils.showSnackBar(String.valueOf(errorCode), tvPassword1);
                AppUtils.dismissLoadingDialog();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initViews(View view) {
        sessionManager = AppUtils.getSessionManagerInstance();

        tvPassword1 = view.findViewById(R.id.tv_password1);
        tvPassword2 = view.findViewById(R.id.tv_password2);
        btnSetPassword = view.findViewById(R.id.btn_set_password);
    }

    private boolean isValidFields() {
        if (tvPassword1.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar("New Password is required", tvPassword1);
            tvPassword1.requestFocus();
            return false;
        }
        if (tvPassword1.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar("Password is too short", tvPassword1);
            tvPassword1.requestFocus();
            return false;
        }

        if (tvPassword2.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar("This is required", tvPassword2);
            tvPassword2.requestFocus();
            return false;
        }

        if (tvPassword2.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar("Password is too short", tvPassword2);
            tvPassword2.requestFocus();
            return false;
        }
        if (!tvPassword1.getText().toString().trim().equals(tvPassword2.getText().toString().trim())) {
            AppUtils.showSnackBar("Passwords do not match", tvPassword1);
            tvPassword2.requestFocus();
            return false;
        }
        return true;
    }

}
