package tingtel.payment.activities.sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.utils.SessionManager;

public class SignUpActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {
    NavController navController;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(navhost);
        sessionManager = AppUtils.getSessionManagerInstance();

        Intent intent = getIntent();
        if (intent.getStringExtra("task") == null) {
            sessionManager.setIsRegistered(false);
        } else {
            sessionManager.setIsRegistered(true);
            if (Objects.requireNonNull(intent.getStringExtra("task")).equalsIgnoreCase("registerSim1")) {
                navController.navigate(R.id.action_signUpHomeFragment_to_signUpSim1Fragment, null);
                AppUtils.showDialog("You need To register Sim 1 before making any transactions", SignUpActivity.this);

            } else if (Objects.requireNonNull(intent.getStringExtra("task")).equalsIgnoreCase("registerSim2")) {
                navController.navigate(R.id.action_signUpHomeFragment_to_signUpSim2Fragment, null);
                AppUtils.showDialog("You need To register Sim 2 before making any transactions", SignUpActivity.this);

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fade_out);
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void doLogout() {
        new Handler(Looper.getMainLooper()).post(() -> AppUtils.logOutInactivitySessionTimeout(this));
    }
}
