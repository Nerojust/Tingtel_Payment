package tingtel.payment.fragments.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import tingtel.payment.MainActivity;
import tingtel.payment.R;
import tingtel.payment.SignUpActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.AppUtils.getSessionManagerInstance;


public class TransferAirtimeHomeFragment extends Fragment {


    Button btnTransferAirtime;
    Button btnHistory;
    NavController navController;
    AppDatabase appDatabase;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Dashboard");

        btnTransferAirtime = view.findViewById(R.id.btn_transfer_airtime);
        btnHistory = view.findViewById(R.id.btn_history);

        appDatabase = AppDatabase.getDatabaseInstance(getContext());
        sessionManager = getSessionManagerInstance();

        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(navhost);


        btnTransferAirtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if both sim 1 and sim 2 are in the db registered.

                if (!sim1ExistsCheck()) {
                    Toast.makeText(getActivity(), "New Sim Detected, You Need to Register this sim on your account", Toast.LENGTH_LONG).show();
                    // navigateToSim1Register();
                    Intent intent = new Intent(getActivity(), SignUpActivity.class);
                    intent.putExtra("task", "registerSim1");
                    startActivity(intent);
                    return;
                }

                if (!sim2ExistsCheck()) {
                    Toast.makeText(getActivity(), "New Sim Detected, You Need to Register this sim on your account", Toast.LENGTH_LONG).show();
                    // navigateToSim2Register();
                    Intent intent = new Intent(getActivity(), SignUpActivity.class);
                    intent.putExtra("task", "registerSim2");
                    startActivity(intent);
                    return;
                }


                navController.navigate(R.id.action_mainFragment_to_transferAirtimeFragment, null);

            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_mainFragment_to_transactionHistoryFragment, null);

            }
        });


        return view;
    }

    private boolean sim1ExistsCheck() {

        String Sim1Serial = sessionManager.getSimSerialICCID();

        if (appDatabase.simCardsDao().getSerial(Sim1Serial).size() > 0) {

            return true;
        } else {
            return false;
        }

    }

    private boolean sim2ExistsCheck() {

        String Sim2Serial = sessionManager.getSimSerialICCID1();

        if (appDatabase.simCardsDao().getSerial(Sim2Serial).size() > 0) {

            return true;
        } else {
            return false;
        }

    }

}
