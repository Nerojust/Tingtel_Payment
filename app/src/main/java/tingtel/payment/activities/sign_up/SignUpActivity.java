package tingtel.payment.activities.sign_up;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;

public class SignUpActivity extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(navhost);

        Intent intent = getIntent();

        if (intent.getStringExtra("task") == null) {

        } else {
            if (Objects.requireNonNull(intent.getStringExtra("task")).equalsIgnoreCase("registerSim1")) {
                navController.navigate(R.id.action_signUpHomeFragment_to_signUpSim1Fragment, null);
                AppUtils.showDialog("You need To register Sim 1 before making any transactions", SignUpActivity.this);

            } else if (Objects.requireNonNull(intent.getStringExtra("task")).equalsIgnoreCase("registerSim2")) {
                navController.navigate(R.id.action_signUpHomeFragment_to_signUpSim2Fragment, null);
                AppUtils.showDialog("You need To register Sim 2 before making any transactions", SignUpActivity.this);

            }
        }
    }
}
