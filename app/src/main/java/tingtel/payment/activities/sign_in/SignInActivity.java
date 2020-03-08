package tingtel.payment.activities.sign_in;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.ForgotPasswordActivity;
import tingtel.payment.activities.sign_up.SignUpActivity;
import tingtel.payment.utils.NetworkCarrierUtils;

public class SignInActivity extends AppCompatActivity {

    private NavController navController;
    private Button btnSingIn;
    private TextView tvSignUp, forgotPasswordTextView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        NetworkCarrierUtils.getCarrierOfSim(this, this);
        initViews();
        initListeners();
    }

    private void initListeners() {
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);

        });

        btnSingIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });
    }

    private void initViews() {
        tvSignUp = findViewById(R.id.tv_signup);
        btnSingIn = findViewById(R.id.btn_sign_in);
        forgotPasswordTextView = findViewById(R.id.forgotPassword);
    }
}
