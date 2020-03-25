package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import tingtel.payment.R;
import tingtel.payment.models.Change_Email.ChangeEmailResponse;
import tingtel.payment.models.Change_Email.ChangeEmailSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.ChangeEmailInterface;

public class ChangeEmailActivity extends AppCompatActivity {

    EditText edCurrentEmail, edNewEmail, edRenterNewEmail;
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
                changeEmailForUser();
            }
        });
    }

    private void initViews() {
        edCurrentEmail = findViewById(R.id.current_email);
        edNewEmail = findViewById(R.id.new_email);
        edRenterNewEmail = findViewById(R.id.re_entered_email);
        btnChangeEmail = findViewById(R.id.btn_change_email);
    }

    private void changeEmailForUser() {
        AppUtils.initLoadingDialog(this);

        ChangeEmailSendObject changeEmailSendObject = new ChangeEmailSendObject();
        changeEmailSendObject.setEmail(edRenterNewEmail.getText().toString().trim());
        //todo: get from login
        changeEmailSendObject.setPhone(sessionManager.getNumberFromLogin());
        changeEmailSendObject.setHash(AppUtils.generateHash(edRenterNewEmail.getText().toString().trim(), sessionManager.getNumberFromLogin()));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(changeEmailSendObject);
        sessionManager.setEmailJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.changeEmailAddress(changeEmailSendObject, new ChangeEmailInterface() {
            @Override
            public void onSuccess(ChangeEmailResponse changeEmailResponse) {
                AppUtils.showDialog("Email Successfully Changed", ChangeEmailActivity.this);
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

    private boolean isValidFields() {
        if (edCurrentEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Current Email is required", Toast.LENGTH_SHORT).show();
            edCurrentEmail.requestFocus();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(edCurrentEmail.getText().toString().trim())) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            edCurrentEmail.requestFocus();
            return false;
        }
        if (edNewEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "New Email is required", Toast.LENGTH_SHORT).show();
            edNewEmail.requestFocus();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(edNewEmail.getText().toString().trim())) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            edNewEmail.requestFocus();
            return false;
        }
        if (edRenterNewEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Field is required", Toast.LENGTH_SHORT).show();
            edRenterNewEmail.requestFocus();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(edRenterNewEmail.getText().toString().trim())) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
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
}
