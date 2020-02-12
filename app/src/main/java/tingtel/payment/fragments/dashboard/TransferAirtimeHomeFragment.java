package tingtel.payment.fragments.dashboard;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tingtel.payment.MainActivity;
import tingtel.payment.R;


public class TransferAirtimeHomeFragment extends Fragment {


   Button btnTransferAirtime;
   Button btnHistory;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Dashboard");

        btnTransferAirtime = view.findViewById(R.id.btn_transfer_airtime);
        btnHistory = view.findViewById(R.id.btn_history);

        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(navhost);


        btnTransferAirtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}
