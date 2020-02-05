package tingtel.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.AppUtils.getSessionManagerInstance;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager = getSessionManagerInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {


            if (sessionManager.getOnboardStatus()) {


                if (sessionManager.getIsLogin()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }


            } else {


                Intent mainIntent = new Intent(SplashActivity.this, OnBoardActivity.class);
                startActivity(mainIntent);
            }

            finish();
        }, 2500);

    }

}

