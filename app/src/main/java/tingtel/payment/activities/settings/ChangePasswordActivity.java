package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edCurrentPassword, edNewPassword, edRenterNewPassword;
    private Button btnChangePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edCurrentPassword = findViewById(R.id.current_password);
        edNewPassword = findViewById(R.id.new_password);
        edRenterNewPassword = findViewById(R.id.re_entered_password);

        btnChangePassword = findViewById(R.id.btn_set_password);
        btnChangePassword.setOnClickListener(v -> AppUtils.showDialog("Password Successfully Changed", this));
    }
}
