package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.Constants;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edCurrentPassword, edNewPassword, edRenterNewPassword;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initViews();
        initListeners();
    }

    private void initListeners() {
        btnChangePassword.setOnClickListener(v -> {
            if (isValidFields()) {
                AppUtils.showDialog("Password Successfully Changed", this);
                //Todo: perform operation
            }
        });
    }

    private void initViews() {
        edCurrentPassword = findViewById(R.id.current_password);
        edNewPassword = findViewById(R.id.new_password);
        edRenterNewPassword = findViewById(R.id.re_entered_password);
        btnChangePassword = findViewById(R.id.btn_set_password);
    }

    private boolean isValidFields() {
        if (edCurrentPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Current Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppUtils.isValidFieldsNumbersAndLetters(edCurrentPassword.getText().toString().trim())) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edCurrentPassword.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar("Password is too short", edCurrentPassword);
            return false;
        }

        if (edNewPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "New Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppUtils.isValidFieldsNumbersAndLetters(edNewPassword.getText().toString().trim())) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edNewPassword.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar("Password is too short", edNewPassword);
            return false;
        }
        if (edRenterNewPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppUtils.isValidFieldsNumbersAndLetters(edRenterNewPassword.getText().toString().trim())) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edRenterNewPassword.getText().toString().trim().length() < Constants.MINIMUM_DIGIT_PASSWORD) {
            AppUtils.showSnackBar("Password is too short", edRenterNewPassword);
            return false;
        }
        if (!edNewPassword.getText().toString().trim().equals(edRenterNewPassword.getText().toString().trim())) {
            AppUtils.showSnackBar("New passwords do not match", edRenterNewPassword);
            return false;
        }

        return true;
    }
}
