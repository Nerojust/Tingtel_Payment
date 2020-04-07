package tingtel.payment.fragments.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.activities.sign_up.SignUpActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;


public class DashboardFragment extends Fragment {

    private NavController navController;
    private AppDatabase appDatabase;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        sessionManager = AppUtils.getSessionManagerInstance();

        sessionManager.setIsRegistered(false);

        ImageView settingsImagview = view.findViewById(R.id.settingsImageview);
        settingsImagview.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));
        TextView customerName = view.findViewById(R.id.customerName);
        //setting the retrieved customer name
        if (sessionManager.getFirstName() != null) {
            customerName.setText("Hi ".concat(sessionManager.getFirstName()));
        } else {
            customerName.setText(getResources().getString(R.string.welcome_customer));
        }
        Button btnTransferAirtime = view.findViewById(R.id.btn_transfer_airtime);
        Button btnHistory = view.findViewById(R.id.btn_history);

        appDatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getContext()));


        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));

        btnTransferAirtime.setOnClickListener(v -> {
            //check if both sim 1 and sim 2 are in the db registered.
            String NoOfSIm = sessionManager.getSimStatus();

            switch (NoOfSIm) {
                case "NO SIM":
                    Intent intent = new Intent(getActivity(), SignUpActivity.class);
                    intent.putExtra("task", "registerSim1");
                    startActivity(intent);

                    break;

                case "SIM1":
                    if (!sim1ExistsCheck()) {
                        Toast.makeText(getActivity(), "New Sim Detected, You need to register this sim on your account", Toast.LENGTH_LONG).show();
                        // navigateToSim1Register();
                        Intent intent2 = new Intent(getActivity(), SignUpActivity.class);
                        intent2.putExtra("task", "registerSim1");
                        startActivity(intent2);
                        return;
                    }
                    break;
                case "SIM1 SIM2":
                    if (!sim1ExistsCheck()) {
                        Toast.makeText(getActivity(), "New Sim Detected, You need to register this sim on your account", Toast.LENGTH_LONG).show();
                        // navigateToSim1Register();
                        Intent intent3 = new Intent(getActivity(), SignUpActivity.class);
                        intent3.putExtra("task", "registerSim1");
                        startActivity(intent3);
                        return;
                    }

                    if (!sim2ExistsCheck()) {
                        Toast.makeText(getActivity(), "New Sim Detected, You need to register this sim on your account", Toast.LENGTH_LONG).show();
                        // navigateToSim2Register();
                        Intent intent4 = new Intent(getActivity(), SignUpActivity.class);
                        intent4.putExtra("task", "registerSim2");
                        startActivity(intent4);
                        return;
                    }
                    break;
            }

            navController.navigate(R.id.action_mainFragment_to_transferAirtimeActivity, null);
        });

        btnHistory.setOnClickListener(v -> {
            navController.navigate(R.id.action_mainFragment_to_historyActivity, null);
            //navController.navigate(R.id.action_mainFragment_to_statusActivity, null);
        });

        return view;
    }

    private boolean sim1ExistsCheck() {
        String Sim1Serial = sessionManager.getSimSerialICCID();
        return appDatabase.simCardsDao().getSerial(Sim1Serial).size() > 0;

    }

    private boolean sim2ExistsCheck() {
        String Sim2Serial = sessionManager.getSimSerialICCID1();

        return appDatabase.simCardsDao().getSerial(Sim2Serial).size() > 0;
    }
}
