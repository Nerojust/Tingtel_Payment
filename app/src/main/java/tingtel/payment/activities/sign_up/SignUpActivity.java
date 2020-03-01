package tingtel.payment.activities.sign_up;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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


            if (intent.getStringExtra("task").equalsIgnoreCase("registerSim1")) {
                navController.navigate(R.id.action_signUpHomeFragment_to_signUpSim1Fragment, null);
                AppUtils.showDialog("You Need To Register Your Sim 1 Network Before You Can Make Any Transactions", SignUpActivity.this);

            } else if (intent.getStringExtra("task").equalsIgnoreCase("registerSim2")) {
                navController.navigate(R.id.action_signUpHomeFragment_to_signUpSim2Fragment, null);
                AppUtils.showDialog("You Need To Register Your Sim 2 Network Before You Can Make Any Transactions", SignUpActivity.this);

            }

        }
    }

    @Override
    public void onBackPressed() {

        if (navController.getCurrentDestination().getId() == R.id.signUpHomeFragment) {


            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setMessage(getResources().getString(R.string.do_you_want_to_exit))
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        finish();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            super.onBackPressed();
        }
    }
}