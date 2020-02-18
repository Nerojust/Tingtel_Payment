package tingtel.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.adapters.SettingsAdapter;

public class SettingsActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Fragment navhost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(navhost);


    }


    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.manageSimsFragment) {
            navController.navigate(R.id.action_manageSimsFragment_to_settingsHome);
        } else if (navController.getCurrentDestination().getId() == R.id.settingsHome) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {super.onBackPressed();}

    }
}
