package tingtel.payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

public class SignInActivity extends AppCompatActivity {

    private NavController navController;
    private Button btnSingIn;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

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
    }

    private void initViews() {
        tvSignUp = findViewById(R.id.tv_signup);
        btnSingIn = findViewById(R.id.btn_sign_in);
    }
}
