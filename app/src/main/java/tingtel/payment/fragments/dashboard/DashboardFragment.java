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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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
    private ImageView settingsImagview;
    private Button btnTransferAirtime, btnHistory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initSettings();
        initViews(view);
        initListeners();

        return view;
    }

    private void initSettings() {
        sessionManager = AppUtils.getSessionManagerInstance();
        sessionManager.setIsRegistered(false);
        appDatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getContext()));
        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }

    private void initViews(View view) {
        MobileAds.initialize(getContext(), initializationStatus -> {
        });

        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        settingsImagview = view.findViewById(R.id.settingsImageview);
        btnTransferAirtime = view.findViewById(R.id.btn_transfer_airtime);
        btnHistory = view.findViewById(R.id.btn_history);
        TextView customerName = view.findViewById(R.id.customerName);

        //setting the retrieved customer name
        if (sessionManager.getFirstName() != null) {
            customerName.setText(getResources().getString(R.string.hi).concat(" ").concat(sessionManager.getFirstName()));
        } else {
            customerName.setText(getResources().getString(R.string.welcome_customer));
        }
    }

    private void initListeners() {
        settingsImagview.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));

        btnTransferAirtime.setOnClickListener(v -> {
            if (sessionManager.getSerialsDontMatchAnyOnDevice()) {
                checkIfSimIsRegistered();
            }
            navController.navigate(R.id.action_mainFragment_to_transferAirtimeActivity, null);
        });

        btnHistory.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_historyActivity, null));
    }

    private void checkIfSimIsRegistered() {
        //sessionManager.setComingFromDashboard(true);

        //check if both sim 1 and sim 2 are in the db registered.
        String NoOfSIm = sessionManager.getSimStatus();

        String REGISTER_SIM_1 = "registerSim1";
        switch (NoOfSIm) {
            case "NO SIM":
                gotoSignUpActivity(REGISTER_SIM_1);
                break;

            case "SIM1":
                if (!sim1ExistsCheck()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.new_sim_detected), Toast.LENGTH_LONG).show();
                    gotoSignUpActivity(REGISTER_SIM_1);
                    return;
                }
                break;
            case "SIM1 SIM2":
                if (!sim1ExistsCheck()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.new_sim_detected), Toast.LENGTH_LONG).show();
                    gotoSignUpActivity(REGISTER_SIM_1);
                    return;
                }

                if (!sim2ExistsCheck()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.new_sim_detected), Toast.LENGTH_LONG).show();
                    String REGISTER_SIM_2 = "registerSim2";
                    gotoSignUpActivity(REGISTER_SIM_2);
                    return;
                }
                break;
        }
    }

    private void gotoSignUpActivity(String simNumber) {
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        intent.putExtra("task", simNumber);
        startActivity(intent);
    }

    private boolean sim1ExistsCheck() {
        String Sim1Serial = sessionManager.getSimSerialICCID();
        if (appDatabase.simCardsDao().getSerial(Sim1Serial).size() > 0) {
            sessionManager.setSimOnePhoneNumber(appDatabase.simCardsDao().getSerial(Sim1Serial).get(0).getPhoneNumber());
            sessionManager.setSimOneNetworkName(appDatabase.simCardsDao().getSerial(Sim1Serial).get(0).getSimNetwork());
            return true;
        } else {
            return false;
        }
    }

    private boolean sim2ExistsCheck() {
        String Sim2Serial = sessionManager.getSimSerialICCID1();
        if (appDatabase.simCardsDao().getSerial(Sim2Serial).size() > 0) {
            sessionManager.setSimTwoPhoneNumber(appDatabase.simCardsDao().getSerial(Sim2Serial).get(0).getPhoneNumber());
            sessionManager.setSimTwoNetworkName(appDatabase.simCardsDao().getSerial(Sim2Serial).get(0).getSimNetwork());
            return true;
        } else {
            return false;
        }
    }
}
