package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.models.change_Email.ChangeEmailResponse;
import tingtel.payment.models.change_Email.ChangeEmailSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.ChangeEmailInterface;

public class ChangeEmailActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {

    EditText edNewEmail, edRenterNewEmail;
    Button btnChangeEmail;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        sessionManager = AppUtils.getSessionManagerInstance();
        initViews();
        initListeners();

    }

    private void initListeners() {
        btnChangeEmail.setOnClickListener(v -> {
            if (isValidFields()) {

                if (AppUtils.isNetworkAvailable(Objects.requireNonNull(this))) {
                    changeEmailForUser();
                } else {
                    AppUtils.showSnackBar("No network available", btnChangeEmail);
                }
            }
        });
    }

    private void initViews() {
        edNewEmail = findViewById(R.id.new_email);
        edRenterNewEmail = findViewById(R.id.re_entered_email);
        btnChangeEmail = findViewById(R.id.btn_change_email);
    }

    private void changeEmailForUser() {
        AppUtils.initLoadingDialog(this);

        ChangeEmailSendObject changeEmailSendObject = new ChangeEmailSendObject();
        changeEmailSendObject.setEmail(edRenterNewEmail.getText().toString().trim());
        changeEmailSendObject.setPhone(sessionManager.getNumberFromLogin());
        changeEmailSendObject.setHash(AppUtils.generateHash(edRenterNewEmail.getText().toString().trim(), sessionManager.getNumberFromLogin()));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(changeEmailSendObject);
        sessionManager.setEmailJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.changeEmailAddress(changeEmailSendObject, new ChangeEmailInterface() {
            @Override
            public void onSuccess(ChangeEmailResponse changeEmailResponse) {
                AppUtils.showDialog(changeEmailResponse.getDescription(), ChangeEmailActivity.this);
                clearViews();
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onError(String error) {
                AppUtils.showSnackBar(error, edRenterNewEmail);
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {

                AppUtils.showSnackBar(String.valueOf(errorCode), edRenterNewEmail);
                AppUtils.dismissLoadingDialog();
            }
        });
    }

    private void clearViews() {
        edNewEmail.setText("");
        edRenterNewEmail.setText("");
        edNewEmail.clearFocus();
        edRenterNewEmail.clearFocus();
    }

    private boolean isValidFields() {
        if (edNewEmail.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar("New Email is required", edNewEmail);
            edNewEmail.requestFocus();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(edNewEmail.getText().toString().trim())) {
            AppUtils.showSnackBar("Invalid email address", edNewEmail);
            edNewEmail.requestFocus();
            return false;
        }
        if (edRenterNewEmail.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar("Field is required", edRenterNewEmail);
            edRenterNewEmail.requestFocus();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(edRenterNewEmail.getText().toString().trim())) {
            AppUtils.showSnackBar("Invalid email address", edRenterNewEmail);
            edRenterNewEmail.requestFocus();
            return false;
        }
        if (!edNewEmail.getText().toString().trim().equals(edRenterNewEmail.getText().toString().trim())) {
            AppUtils.showSnackBar("New Email fields do not match", edRenterNewEmail);
            edRenterNewEmail.requestFocus();
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
