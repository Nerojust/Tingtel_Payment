package tingtel.payment.activities.onboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.R;
import tingtel.payment.activities.sign_in.SignInActivity;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.AppUtils.getSessionManagerInstance;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager = getSessionManagerInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        String reg = sessionManager.getRegistrationJsonObject();
        String login = sessionManager.getLoginJsonObject();
        String pass = sessionManager.getPasswordJsonObject();
        String email = sessionManager.getEmailJsonObject();
        String reportIssue = sessionManager.getReportIssueJsonObject();

        new Handler().postDelayed(() -> {
            if (sessionManager.getOnboardStatus()) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Intent mainIntent = new Intent(SplashActivity.this, OnBoardActivity.class);
                startActivity(mainIntent);
            }

            finish();
        }, 2500);
    }
}

