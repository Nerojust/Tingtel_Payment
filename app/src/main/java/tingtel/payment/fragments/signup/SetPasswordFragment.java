package tingtel.payment.fragments.signup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import tingtel.payment.models.registration.CustomerRegistrationResponse;
import tingtel.payment.models.registration.CustomerRegistrationSendObject;
import tingtel.payment.models.registration.Sim1;
import tingtel.payment.models.registration.Sim2;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_password, container, false);

        initViews(view);
        initListeners();

        return view;
    }

    private void initListeners() {
        btnSetPassword.setOnClickListener(v -> {
            if (isValidFields()) {

                if (AppUtils.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
                    registerUser();
                } else {
                    AppUtils.showSnackBar(getResources().getString(R.string.no_network_available), tvPassword1);
                }
            }
        });

    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Objects.requireNonNull(getActivity()).startActivity(intent);

        getActivity().finish();
    }

    private void registerUser() {
        AppUtils.initLoadingDialog(getContext());

        Sim1 sim1 = new Sim1();
        sim1.setPhone(sessionManager.getSimOnePhoneNumber());
        sim1.setUserNetwork(sessionManager.getUserNetwork());
        sim1.setSerialNumber(sessionManager.getSimSerialICCID());

        Sim2 sim2 = new Sim2();
        sim2.setPhone(sessionManager.getSimTwoPhoneNumber());
        sim2.setUserNetwork(sessionManager.getSimTwoNetworkName());
        sim2.setSerialNumber(sessionManager.getSimSerialICCID1());

        CustomerRegistrationSendObject customerRegistrationSendObject = new CustomerRegistrationSendObject.Builder()
                .firstName(sessionManager.getFirstName())
                .lastName(sessionManager.getLastNameFromLogin())
                .email(sessionManager.getEmailAddress())
                .username(sessionManager.getUserame())
                .sim1(sim1)
                .sim2(sim2)
                .password(Objects.requireNonNull(tvPassword1.getText()).toString().trim())
                .hash(AppUtils.generateHash(sessionManager.getEmailAddress(), sessionManager.getSimOnePhoneNumber()))
                .build();

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
                Toast.makeText(getContext(), getResources().getString(R.string.please_login_with_new_details), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                if (error.equalsIgnoreCase(getResources().getString(R.string.invalid_hash_key))) {
                    AppUtils.showSnackBar(getResources().getString(R.string.server_error_try_again) + error, tvPassword1);
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
        tvPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 5) {
                    AppUtils.changeStatusOfButton(Objects.requireNonNull(getContext()), btnSetPassword, true);
                } else {
                    AppUtils.changeStatusOfButton(Objects.requireNonNull(getContext()), btnSetPassword, false);
                }
            }
        });
        btnSetPassword = view.findViewById(R.id.btn_set_password);
    }

    private boolean isValidFields() {
        if (Objects.requireNonNull(tvPassword1.getText()).toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.this_is_required), tvPassword1);
            tvPassword1.requestFocus();
            return false;
        }
        if (tvPassword1.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar(getResources().getString(R.string.password_is_too_short), tvPassword1);
            tvPassword1.requestFocus();
            return false;
        }

        if (Objects.requireNonNull(tvPassword2.getText()).toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.this_is_required), tvPassword2);
            tvPassword2.requestFocus();
            return false;
        }

        if (tvPassword2.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar(getResources().getString(R.string.password_is_too_short), tvPassword2);
            tvPassword2.requestFocus();
            return false;
        }
        if (!tvPassword1.getText().toString().trim().equals(tvPassword2.getText().toString().trim())) {
            AppUtils.showSnackBar(getResources().getString(R.string.password_do_not_match), tvPassword1);
            tvPassword2.requestFocus();
            return false;
        }
        return true;
    }

}
