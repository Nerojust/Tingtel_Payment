package tingtel.payment.activities.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        NetworkCarrierUtils.getCarrierOfSim(this, this);
        initViews();
        initListeners();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

      //  Toast.makeText(this, "Height is : "+height +"\n"+"width is : "+width, Toast.LENGTH_SHORT).show();
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
