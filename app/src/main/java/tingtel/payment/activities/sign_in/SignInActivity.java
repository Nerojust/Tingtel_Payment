package tingtel.payment.activities.sign_in;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.security.KeyStore;
import java.util.Objects;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.ForgotPasswordActivity;
import tingtel.payment.activities.sign_up.SignUpActivity;
import tingtel.payment.models.login.CustomerLoginResponse;
import tingtel.payment.models.login.CustomerLoginSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.Constants;
import tingtel.payment.utils.GPSutils;
import tingtel.payment.utils.NetworkCarrierUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.LoginResponseInterface;

public class SignInActivity extends GPSutils {

    private static final String KEY_NAME = "Tingtel";
    private static final String TAG = "Signinactivity";
    private NavController navController;
    private Button btnSingIn, tvSignUp;
    private TextView forgotPasswordTextView, fingerprintTextview;
    private TextInputEditText usernameEditext, passwordEditext;
    private CheckBox rememberMeCheckbox;
    private SessionManager sessionManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private String data;
    private byte[] encryptedText;

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
        startFingerprintAuthentication();
    }

    private void startFingerprintAuthentication() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                fingerprintTextview.setVisibility(View.VISIBLE);
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");

                Executor executor = ContextCompat.getMainExecutor(this);
                biometricPrompt = new BiometricPrompt(this,
                        executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        //Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                        usernameEditext.requestFocus();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                        if (AppUtils.isNetworkAvailable(Objects.requireNonNull(SignInActivity.this))) {
                            loginUserWithFingerprint();
                        } else {
                            AppUtils.showSnackBar("No network available", passwordEditext);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                //todo: show a different layout
                fingerprintTextview.setVisibility(View.GONE);

                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                fingerprintTextview.setVisibility(View.GONE);
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("MY_APP_TAG", "The user hasn't associated " +
                        "any biometric credentials with their account.");
                Toast.makeText(this, "Please register a fingerprint in settings", Toast.LENGTH_SHORT).show();
                break;
        }


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for Tingtel")
                .setSubtitle("Log in using your fingerprint")
                .setNegativeButtonText("No, thanks.")
                .build();

    }


    private void initListeners() {
        fingerprintTextview.setOnClickListener(view -> {
            if (sessionManager.getPassword() != null) {
                biometricPrompt.authenticate(promptInfo);
            } else {
                AppUtils.showDialog("Please login with your password first to use this", this);
            }
        });
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        btnSingIn.setOnClickListener(v -> {
            if (AppUtils.isNetworkAvailable(this)) {
                if (AppUtils.isLocationEnabled(this)) {
                    if (isValidFields()) {
                        if (AppUtils.isNetworkAvailable(Objects.requireNonNull(this))) {
                            sessionManager.setRememberLoginCheck(rememberMeCheckbox.isChecked());
                            sessionManager.setUsername(Objects.requireNonNull(usernameEditext.getText()).toString().trim());

                            loginUserWithPassword();
                        } else {
                            AppUtils.showSnackBar("No network available", passwordEditext);
                        }
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
    private void loginUserWithPassword() {
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
                sessionManager.setPassword(password);
                sessionManager.setIsRegistered(true);
                saveDetailsToPref(loginResponses);


                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onError(String error) {
                if (error.equalsIgnoreCase("Invalid Hash Key"))
                    AppUtils.showSnackBar("Invalid credentials", usernameEditext);
                else {
                    if (error.contains("failed to connect")) {
                        AppUtils.showSnackBar("Network error, please try again", usernameEditext);
                    } else {
                        AppUtils.showSnackBar(error, usernameEditext);
                    }
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

    private void saveDetailsToPref(CustomerLoginResponse loginResponses) {
        sessionManager.setFirstName(loginResponses.getUserInfo().get(0).getFirstName());
        sessionManager.setEmailFromLogin(loginResponses.getUserInfo().get(0).getEmail());
        sessionManager.setNumberFromLogin(loginResponses.getUserInfo().get(0).getPhone());
    }

    /**
     * this method handles the logging in of the user.
     */
    private void loginUserWithFingerprint() {
        AppUtils.initLoadingDialog(this);

        String username = Objects.requireNonNull(usernameEditext.getText()).toString().trim();
        String password = Objects.requireNonNull(sessionManager.getPassword());

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
                saveDetailsToPref(loginResponses);

                sessionManager.setIsRegistered(true);

                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onError(String error) {
                if (error.equalsIgnoreCase("Invalid Hash Key"))
                    AppUtils.showSnackBar("Invalid credentials", usernameEditext);
                else {
                    if (error.contains("failed to connect")) {
                        AppUtils.showSnackBar("Network error, please try again", usernameEditext);
                    } else {
                        AppUtils.showSnackBar(error, usernameEditext);
                    }
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
                    loginUserWithPassword();
                }
            }
            return false;
        });
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        fingerprintTextview = findViewById(R.id.fingerprint);
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

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fragment_open_enter, R.anim.fade_out);
    }

    interface MessageDialogInterface {
        void onClick();
    }
}
