package tingtel.payment.activities.sign_in;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Objects;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.forgot_password.ForgotPasswordActivity;
import tingtel.payment.activities.sign_up.SignUpActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;
import tingtel.payment.models.login.CustomerLoginResponse;
import tingtel.payment.models.login.CustomerLoginSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.Constants;
import tingtel.payment.utils.Cryptography;
import tingtel.payment.utils.NetworkCarrierUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.LoginResponseInterface;

public class SignInActivity extends AppCompatActivity {

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
    private String serial1Login;
    private String serial2Login;
    private String serial3Login;
    private String serial4Login;
    private String sim1SerialOnDevice;
    private String sim2SerialOnDevice;
    private String phone1Login;
    private AppDatabase appDatabase;
    private String phone2Login;
    private String phone3Login;
    private String phone4Login;
    @SuppressLint({"NewApi", "LocalSuppress"})
    private Cryptography cryptography;
    private String encryptedPassword;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        appDatabase = AppDatabase.getDatabaseInstance(this);
        sessionManager = AppUtils.getSessionManagerInstance();
        NetworkCarrierUtils.getCarrierOfSim(this, this);

        initViews();
        try {
            initListeners();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        checkBoxRememberSettings();
        startFingerprintAuthentication();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                        usernameEditext.requestFocus();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);

                        if (AppUtils.isNetworkAvailable(Objects.requireNonNull(SignInActivity.this))) {
                            loginUserWithFingerprint();
                        } else {
                            AppUtils.showSnackBar(getResources().getString(R.string.no_network_available), passwordEditext);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        //Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(this, "Please register a fingerprint in settings", Toast.LENGTH_SHORT).show();
                break;
        }


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.biometric_login_for_tingtel))
                .setSubtitle(getResources().getString(R.string.login_with_fingerprint))
                .setNegativeButtonText(getResources().getString(R.string.no_thanks))
                .build();

    }


    @SuppressLint("NewApi")
    private void initListeners() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        cryptography = new Cryptography(KEY_NAME);

        fingerprintTextview.setOnClickListener(view -> {
            if (sessionManager.getPassword() != null && !Objects.requireNonNull(usernameEditext.getText()).toString().isEmpty()) {
                biometricPrompt.authenticate(promptInfo);
            } else {
                AppUtils.showDialog(getResources().getString(R.string.please_login_with_your_credentials_to_use_this), this);
            }
        });
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        btnSingIn.setOnClickListener(v -> {
            if (AppUtils.isNetworkAvailable(this)) {
                if (isValidFields()) {
                    if (AppUtils.isNetworkAvailable(Objects.requireNonNull(this))) {
                        sessionManager.setRememberLoginCheck(rememberMeCheckbox.isChecked());
                        sessionManager.setUsername(Objects.requireNonNull(usernameEditext.getText()).toString().trim());

                        loginUserWithPassword();
                    } else {
                        AppUtils.showSnackBar(getResources().getString(R.string.no_network_available), passwordEditext);
                    }
                }
            } else {
                AppUtils.showSnackBar(getResources().getString(R.string.no_network_available), usernameEditext);
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
        loginSendObject.setHash(AppUtils.generateHash(username, password));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(loginSendObject);
        AppUtils.getSessionManagerInstance().setLoginJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.loginInUser(loginSendObject, new LoginResponseInterface() {
            @SuppressLint("NewApi")
            @Override
            public void onSuccess(CustomerLoginResponse loginResponses) {
                sessionManager.startLoginSession();
                sessionManager.setPassword(password);

                encryptPasswordDetails(password);

                saveDetailsToPrefFromLoginResponse(loginResponses);

                compareSerialsFromResponse(loginResponses);

                gotoMainActivity();
                AppUtils.dismissLoadingDialog();
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

    private void gotoMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("NewApi")
    private void encryptPasswordDetails(String password) {
        try {
            encryptedPassword = cryptography.encrypt(password);
            sessionManager.setEncryptedPassword(encryptedPassword);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    private void compareSerialsFromResponse(CustomerLoginResponse loginResponse) {

        sim1SerialOnDevice = sessionManager.getSimSerialICCID();
        sim2SerialOnDevice = sessionManager.getSimSerialICCID1();

        serial1Login = loginResponse.getSim1().get(0).getSim1Serial();
        serial2Login = loginResponse.getSim2().get(0).getSim2Serial();
        serial3Login = loginResponse.getSim3().get(0).getSim3Serial();
        serial4Login = loginResponse.getSim4().get(0).getSim4Serial();

        phone1Login = loginResponse.getSim1().get(0).getPhone();
        phone2Login = loginResponse.getSim2().get(0).getPhone2();
        phone3Login = loginResponse.getSim3().get(0).getPhone3();
        phone4Login = loginResponse.getSim4().get(0).getPhone4();

        String NoOfSIm = sessionManager.getSimStatus();

        boolean serial1Correlated = isSerialCorrelated(serial1Login, sim1SerialOnDevice);
        boolean serial2Correlated = isSerialCorrelated(serial2Login, sim1SerialOnDevice);
        boolean serial3Correlated = isSerialCorrelated(serial3Login, sim1SerialOnDevice);
        boolean serial4Correlated = isSerialCorrelated(serial4Login, sim1SerialOnDevice);

        switch (NoOfSIm) {
            case "NO SIM":

                break;

            case "SIM1":
                if (serial1Correlated) {
                    sessionManager.setSimOnePhoneNumber(phone1Login);
                } else if (serial2Correlated) {
                    sessionManager.setSimOnePhoneNumber(phone2Login);
                } else if (serial3Correlated) {
                    sessionManager.setSimOnePhoneNumber(phone3Login);
                } else if (serial4Correlated) {
                    sessionManager.setSimOnePhoneNumber(phone4Login);
                } else {
                    //either null or not a match found
                    sessionManager.setSerialsDontMatchAnyOnDevice(true);
                }
                break;
            case "SIM1 SIM2":
                if (serial1Correlated) {
                    sessionManager.setSimOnePhoneNumber(phone1Login);
                } else if (serial2Correlated) {
                    sessionManager.setSimOnePhoneNumber(phone2Login);
                } else if (serial3Correlated) {
                    sessionManager.setSimOnePhoneNumber(phone3Login);
                } else if (serial4Correlated) {
                    sessionManager.setSimOnePhoneNumber(phone4Login);
                } else {
                    //either null or not a match found
                    sessionManager.setSerialsDontMatchAnyOnDevice(true);
                }

                boolean serial1Correlated1 = isSerialCorrelated(serial1Login, sim2SerialOnDevice);
                boolean serial2Correlated1 = isSerialCorrelated(serial2Login, sim2SerialOnDevice);
                boolean serial3Correlated1 = isSerialCorrelated(serial3Login, sim2SerialOnDevice);
                boolean serial4Correlated1 = isSerialCorrelated(serial4Login, sim2SerialOnDevice);

                if (serial1Correlated1) {
                    sessionManager.setSimTwoPhoneNumber(phone1Login);
                } else if (serial2Correlated1) {
                    sessionManager.setSimTwoPhoneNumber(phone2Login);
                } else if (serial3Correlated1) {
                    sessionManager.setSimTwoPhoneNumber(phone3Login);
                } else if (serial4Correlated1) {
                    sessionManager.setSimTwoPhoneNumber(phone4Login);
                } else {
                    //either null or not a match found
                    sessionManager.setSerialsDontMatchAnyOnDevice(true);
                }
                break;
        }
    }

    private boolean isSerialCorrelated(String loginSerial, String deviceSerial) {
        if (loginSerial != null) {
            return loginSerial.equalsIgnoreCase(deviceSerial);
        }
        return false;
    }

    private void saveDetailsToPrefFromLoginResponse(CustomerLoginResponse loginResponses) {
        //name, email and number
        sessionManager.setFirstNameFromLogin(loginResponses.getUserInfo().get(0).getFirstName());
        sessionManager.setEmailFromLogin(loginResponses.getUserInfo().get(0).getEmail());
        sessionManager.setNumberFromLogin(loginResponses.getUserInfo().get(0).getPhone());

        appDatabase.simCardsDao().delete();

        if (loginResponses.getSim1().get(0).getPhone() != null) {
            SimCards sim = new SimCards();
            sim.setPhoneNumber(loginResponses.getSim1().get(0).getPhone());
            sim.setSimSerial(loginResponses.getSim1().get(0).getSim1Serial());
            sim.setSimNetwork(loginResponses.getSim1().get(0).getUserNetwork());
            //adding to database
            appDatabase.simCardsDao().insert(sim);
            Log.e("TingtelApp", "number is" + loginResponses.getSim1().get(0).getPhone());
        }
        if (loginResponses.getSim2().get(0).getPhone2() != null) {
            SimCards sim = new SimCards();
            sim.setPhoneNumber(loginResponses.getSim2().get(0).getPhone2());
            sim.setSimSerial(loginResponses.getSim2().get(0).getSim2Serial());
            sim.setSimNetwork(loginResponses.getSim2().get(0).getSim2UserNetwork());
            //adding to database
            appDatabase.simCardsDao().insert(sim);
            Log.e("TingtelApp", "number is" + loginResponses.getSim2().get(0).getPhone2());
        }


        if (loginResponses.getSim3().get(0).getPhone3() != null) {
            SimCards sim = new SimCards();
            sim.setPhoneNumber(loginResponses.getSim3().get(0).getPhone3());
            sim.setSimSerial(loginResponses.getSim3().get(0).getSim3Serial());
            sim.setSimNetwork(loginResponses.getSim3().get(0).getSim3UserNetwork());
            //adding to database
            appDatabase.simCardsDao().insert(sim);
            Log.e("TingtelApp", "number is" + loginResponses.getSim3().get(0).getPhone3());
        }
        if (loginResponses.getSim4().get(0).getPhone4() != null) {
            SimCards sim = new SimCards();
            sim.setPhoneNumber(loginResponses.getSim4().get(0).getPhone4());
            sim.setSimSerial(loginResponses.getSim4().get(0).getSim4Serial());
            sim.setSimNetwork(loginResponses.getSim4().get(0).getSim4UserNetwork());
            //adding to database
            appDatabase.simCardsDao().insert(sim);
            Log.e("TingtelApp", "number is" + loginResponses.getSim4().get(0).getPhone4());
        }

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
        loginSendObject.setHash(AppUtils.generateHash(username, password));


        Gson gson = new Gson();
        String jsonObject = gson.toJson(loginSendObject);
        AppUtils.getSessionManagerInstance().setLoginJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.loginInUser(loginSendObject, new LoginResponseInterface() {
            @Override
            public void onSuccess(CustomerLoginResponse loginResponses) {
                AppUtils.dismissLoadingDialog();
                sessionManager.startLoginSession();
                saveDetailsToPrefFromLoginResponse(loginResponses);

                compareSerialsFromResponse(loginResponses);

                gotoMainActivity();
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
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        usernameEditext = findViewById(R.id.tv_username);
        passwordEditext = findViewById(R.id.tv_password);
        passwordEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 4 && !Objects.requireNonNull(usernameEditext.getText()).toString().isEmpty()) {
                    AppUtils.changeStatusOfButton(Objects.requireNonNull(SignInActivity.this), btnSingIn, true);
                } else {
                    AppUtils.changeStatusOfButton(Objects.requireNonNull(SignInActivity.this), btnSingIn, false);
                }

            }
        });
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
            AppUtils.showSnackBar(getResources().getString(R.string.username_is_required), usernameEditext);
            usernameEditext.requestFocus();
            return false;
        }
        if (!AppUtils.isValidFieldsNumbersAndLetters(usernameEditext.getText().toString().trim())) {
            AppUtils.showSnackBar(getResources().getString(R.string.invalid_characters), usernameEditext);
            usernameEditext.requestFocus();
            return false;
        }

        if (Objects.requireNonNull(passwordEditext.getText()).toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.password_is_required), passwordEditext);
            passwordEditext.requestFocus();
            return false;
        }

        if (passwordEditext.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar(getResources().getString(R.string.password_is_too_short), passwordEditext);
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
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, id) -> {
                    finishAffinity();
                    SignInActivity.this.onSuperBackPressed();
                })
                .setNegativeButton(getResources().getString(R.string.no), (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void onSuperBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fade_out);
    }

    interface MessageDialogInterface {
        void onClick();
    }
}
