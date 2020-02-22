package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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


        edCurrentEmail = findViewById(R.id.current_email);
        edNewEmail = findViewById(R.id.new_email);
        edRenterNewEmail = findViewById(R.id.re_entered_email);

        btnChangeEmail = findViewById(R.id.btn_change_email);
        btnChangeEmail.setOnClickListener(v -> AppUtils.showDialog("Email Successfully Changed", this));

    }
}
