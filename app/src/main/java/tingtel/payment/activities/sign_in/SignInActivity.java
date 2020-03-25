package tingtel.payment.activities.sign_in;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.ForgotPasswordActivity;
import tingtel.payment.activities.sign_up.SignUpActivity;
import tingtel.payment.models.Login.CustomerLoginResponse;
import tingtel.payment.models.Login.CustomerLoginSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.Constants;
import tingtel.payment.utils.GPSutils;
import tingtel.payment.utils.NetworkCarrierUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.LoginResponseInterface;

public class SignInActivity extends GPSutils {

    private NavController navController;
    private Button btnSingIn;
    private TextView tvSignUp, forgotPasswordTextView;
    private TextInputEditText usernameEditext, passwordEditext;
    private CheckBox rememberMeCheckbox;
    private SessionManager sessionManager;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sessionManager = AppUtils.getSessionManagerInstance();
        NetworkCarrierUtils.getCarrierOfSim(this, this);

        initViews();
        initListeners();
        checkBoxRememberSettings();

    }

    private void initListeners() {
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);

        });

        btnSingIn.setOnClickListener(v -> {
            if (AppUtils.isNetworkAvailable(this)) {
                if (AppUtils.isLocationEnabled(this)) {
                    if (isValidFields()) {
                        boolean checked = rememberMeCheckbox.isChecked();
                        sessionManager.setRememberLoginCheck(checked);
                        sessionManager.setUsername(usernameEditext.getText().toString().trim());
                        loginUser();
                    }
                } else {
                    showDialogMessage(getString(R.string.put_on_your_gps), () -> {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        this.startActivity(myIntent);
                    });
                }
                //todo:uncomment later before making network call
            } else {
                AppUtils.showSnackBar("No network available", usernameEditext);
            }
        });


        forgotPasswordTextView.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    /**
     * this method handles the logging in of the user.
     */
    private void loginUser() {
        AppUtils.initLoadingDialog(this);

        String username = Objects.requireNonNull(usernameEditext.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordEditext.getText()).toString().trim();

        CustomerLoginSendObject loginSendObject = new CustomerLoginSendObject();
        loginSendObject.setUsername(username);
        loginSendObject.setPassword(password);
        loginSendObject.setHash(AppUtils.getSHA512(username + password + BuildConfig.HASH_KEY));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(loginSendObject);
        AppUtils.getSessionManagerInstance().setLoginJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.loginInUser(loginSendObject, new LoginResponseInterface() {
            @Override
            public void onSuccess(CustomerLoginResponse loginResponses) {
                AppUtils.dismissLoadingDialog();
                sessionManager.setFirstName(loginResponses.getUserInfo().get(0).getFirstName());

                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onError(String error) {
                if (error.equalsIgnoreCase("Invalid Hash Key"))
                    AppUtils.showSnackBar("Invalid credentials", usernameEditext);
                else {
                    AppUtils.showSnackBar(error, usernameEditext);
                }

                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.dismissLoadingDialog();
                AppUtils.showSnackBar(String.valueOf(errorCode), usernameEditext);
            }
        });

    }

    private void showDialogMessage(String msg, MessageDialogInterface messageDialogInterface) {
        new MaterialDialog.Builder(this)
                .content(msg)
                .positiveText("Ok")
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .onPositive((dialog, which) -> messageDialogInterface.onClick())
                .show();
    }

    private void initViews() {
        usernameEditext = findViewById(R.id.tv_username);
        passwordEditext = findViewById(R.id.tv_password);

        passwordEditext.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isValidFields()) {
                    loginUser();
                }
            }
            return false;
        });
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        tvSignUp = findViewById(R.id.tv_signup);
        btnSingIn = findViewById(R.id.btn_sign_in);
        forgotPasswordTextView = findViewById(R.id.forgotPassword);

    }

    private void checkBoxRememberSettings() {
        if (sessionManager.getRememberLoginCheck()) {
            String username = sessionManager.getUserame();
            rememberMeCheckbox.setChecked(true);
            usernameEditext.setText(username);
        } else {
            usernameEditext.setText("");
            rememberMeCheckbox.setChecked(false);
        }
    }

    private boolean isValidFields() {
        if (Objects.requireNonNull(usernameEditext.getText()).toString().trim().isEmpty()) {
            AppUtils.showSnackBar("Username is required", usernameEditext);
            usernameEditext.requestFocus();
            return false;
        }
        if (!AppUtils.isValidFieldsNumbersAndLetters(usernameEditext.getText().toString().trim())) {
            AppUtils.showSnackBar("Invalid character/s detected", usernameEditext);
            usernameEditext.requestFocus();
            return false;
        }

        if (Objects.requireNonNull(passwordEditext.getText()).toString().trim().isEmpty()) {
            AppUtils.showSnackBar("Password is required", passwordEditext);
            passwordEditext.requestFocus();
            return false;
        }

        if (passwordEditext.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar("Password is too short", passwordEditext);
            passwordEditext.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.do_you_want_to_exit))
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    finishAffinity();
                    SignInActivity.this.onSuperBackPressed();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void onSuperBackPressed() {
        super.onBackPressed();
    }

    interface MessageDialogInterface {
        void onClick();
    }
}
