package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.models.change_Password.ChangePasswordResponse;
import tingtel.payment.models.change_Password.ChangePasswordSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.Constants;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.ChangePasswordInterface;

public class ChangePasswordActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {

    private EditText edNewPassword, edRenterNewPassword;
    private Button btnChangePassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        sessionManager = AppUtils.getSessionManagerInstance();

        initViews();
        initListeners();
    }

    private void initListeners() {
        btnChangePassword.setOnClickListener(v -> {
            if (isValidFields()) {
                if (AppUtils.isNetworkAvailable(Objects.requireNonNull(this))) {
                    changePasswordForUser();
                } else {
                    AppUtils.showSnackBar(getResources().getString(R.string.no_network_available), btnChangePassword);
                }
            }
        });
    }

    private void initViews() {
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        edNewPassword = findViewById(R.id.new_password);
        edRenterNewPassword = findViewById(R.id.re_entered_password);
        btnChangePassword = findViewById(R.id.btn_set_password);
    }

    private void changePasswordForUser() {
        AppUtils.initLoadingDialog(this);

        ChangePasswordSendObject changePasswordSendObject = new ChangePasswordSendObject();
        changePasswordSendObject.setEmail(sessionManager.getEmailFromLogin());
        changePasswordSendObject.setPhone(sessionManager.getNumberFromLogin());
        changePasswordSendObject.setHash(AppUtils.generateHash(sessionManager.getEmailFromLogin(), sessionManager.getNumberFromLogin()));
        changePasswordSendObject.setPassword(edRenterNewPassword.getText().toString().trim());

        Gson gson = new Gson();
        String jsonObject = gson.toJson(changePasswordSendObject);
        sessionManager.setPasswordJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.changePassword(changePasswordSendObject, new ChangePasswordInterface() {
            @Override
            public void onSuccess(ChangePasswordResponse changePasswordResponse) {
                AppUtils.showDialog(getResources().getString(R.string.password_changed_successfully), ChangePasswordActivity.this);
                clearViews();
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onError(String error) {
                AppUtils.showSnackBar(error, edRenterNewPassword);
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showSnackBar(String.valueOf(errorCode), edRenterNewPassword);
                AppUtils.dismissLoadingDialog();
            }
        });
    }

    private void clearViews() {
        edNewPassword.setText("");
        edRenterNewPassword.setText("");
        edNewPassword.clearFocus();
        edRenterNewPassword.clearFocus();
    }


    private boolean isValidFields() {
        if (edNewPassword.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.this_is_required), edNewPassword);
            edNewPassword.requestFocus();
            return false;
        }
        if (!AppUtils.isValidFieldsNumbersAndLetters(edNewPassword.getText().toString().trim())) {
            AppUtils.showSnackBar(getResources().getString(R.string.invalid_characters), edNewPassword);
            edNewPassword.requestFocus();
            return false;
        }
        if (edNewPassword.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar(getResources().getString(R.string.password_is_too_short), edNewPassword);
            edNewPassword.requestFocus();
            return false;
        }
        if (edRenterNewPassword.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.this_is_required), edRenterNewPassword);
            edRenterNewPassword.requestFocus();
            return false;
        }
        if (!AppUtils.isValidFieldsNumbersAndLetters(edRenterNewPassword.getText().toString().trim())) {
            AppUtils.showSnackBar(getResources().getString(R.string.invalid_characters), edRenterNewPassword);
            edRenterNewPassword.requestFocus();
            return false;
        }
        if (edRenterNewPassword.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar(getResources().getString(R.string.password_is_too_short), edRenterNewPassword);
            edRenterNewPassword.requestFocus();
            return false;
        }
        if (!edNewPassword.getText().toString().trim().equals(edRenterNewPassword.getText().toString().trim())) {
            AppUtils.showSnackBar(getResources().getString(R.string.password_do_not_match), edRenterNewPassword);
            edRenterNewPassword.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fade_out);
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void doLogout() {
        new Handler(Looper.getMainLooper()).post(() -> AppUtils.logOutInactivitySessionTimeout(this));
    }
}
