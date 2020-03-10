package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;

public class ChangeEmailActivity extends AppCompatActivity {

    EditText edCurrentEmail, edNewEmail, edRenterNewEmail;
    Button btnChangeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        initViews();
        initListeners();

    }

    private void initListeners() {
        btnChangeEmail.setOnClickListener(v -> {
            if (isValidFields()) {
                AppUtils.showDialog("Email Successfully Changed", this);
                //Todo: perform operation
            }
        });
    }

    private void initViews() {
        edCurrentEmail = findViewById(R.id.current_email);
        edNewEmail = findViewById(R.id.new_email);
        edRenterNewEmail = findViewById(R.id.re_entered_email);
        btnChangeEmail = findViewById(R.id.btn_change_email);
    }

    private boolean isValidFields() {
        if (edCurrentEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Current Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(edCurrentEmail.getText().toString().trim())) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edNewEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "New Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(edNewEmail.getText().toString().trim())) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edRenterNewEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(edRenterNewEmail.getText().toString().trim())) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
