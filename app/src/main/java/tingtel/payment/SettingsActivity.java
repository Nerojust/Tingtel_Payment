package tingtel.payment;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    NavController navController;
    LinearLayout backArrowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backArrowLayout = findViewById(R.id.backArrowLayout);
        Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));


        backArrowLayout.setOnClickListener(v -> this.onBackPressed());

    }


    @Override
    public void onBackPressed() {
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.manageSimsFragment) {
            navController.navigate(R.id.action_manageSimsFragment_to_settingsHome);

        } else if (navController.getCurrentDestination().getId() == R.id.settingsHome) {
//            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
//            startActivity(intent);
            finish();

        } else {
            super.onBackPressed();
        }
    }
}
