package tingtel.payment.fragments.forgot_password;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.sign_in.SignInActivity;
import tingtel.payment.models.forgot_password.ForgotPasswordResponse;
import tingtel.payment.models.forgot_password.ForgotPasswordSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.Constants;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.ForgotPasswordInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordSetNewPasswordFragment extends Fragment {
    private SessionManager sessionManager;
    private TextInputEditText tvPassword1;
    private TextInputEditText tvPassword2;
    private Button btnSetPassword;
    private String retrievedPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password_set_password, container, false);

        initViews(view);
        initListeners();

        return view;
    }

    private void initListeners() {
        btnSetPassword.setOnClickListener(v -> {
            if (isValidFields()) {
                changePasswordForUser();
            }
        });
    }

    private void changePasswordForUser() {
        AppUtils.initLoadingDialog(getContext());

        ForgotPasswordSendObject changePasswordSendObject = new ForgotPasswordSendObject();
        changePasswordSendObject.setPhone(retrievedPhone);
        changePasswordSendObject.setHash(AppUtils.generateHash("", retrievedPhone));
        changePasswordSendObject.setPassword(Objects.requireNonNull(tvPassword1.getText()).toString().trim());

        Gson gson = new Gson();
        String jsonObject = gson.toJson(changePasswordSendObject);
        sessionManager.setPasswordJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.forgotPassword(changePasswordSendObject, new ForgotPasswordInterface() {
            @Override
            public void onSuccess(ForgotPasswordResponse forgotPasswordResponse) {

                Toast.makeText(getContext(), "Successful. Please login in with new password", Toast.LENGTH_LONG).show();
                goToLoginActivity();
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onError(String error) {
                AppUtils.showSnackBar(error, tvPassword1);
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showSnackBar(String.valueOf(errorCode), tvPassword1);
                AppUtils.dismissLoadingDialog();
            }
        });
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requireActivity().startActivity(intent);
        requireActivity().finish();
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
        //Toast.makeText(getContext(), "Password set successfully", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void initViews(View view) {
        tvPassword1 = view.findViewById(R.id.tv_password1);
        tvPassword2 = view.findViewById(R.id.tv_password2);
        btnSetPassword = view.findViewById(R.id.btn_set_password);

        retrievedPhone = getArguments().getString("phone");
        sessionManager = AppUtils.getSessionManagerInstance();
    }
}
