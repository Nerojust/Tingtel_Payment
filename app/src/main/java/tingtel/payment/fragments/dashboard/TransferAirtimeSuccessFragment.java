package tingtel.payment.fragments.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import tingtel.payment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferAirtimeSuccessFragment extends Fragment {

    Button btnSendNotification;
    Button btnMakeAnotherTransfer;
    Button btnViewHistory;
    NavController navController;
    String ReceiverPhoneNumber;
    String Amount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_airtime_success, container, false);

        btnSendNotification = view.findViewById(R.id.btn_send_notification);
        btnMakeAnotherTransfer = view.findViewById(R.id.btn_make_another_transfer);
        btnViewHistory = view.findViewById(R.id.btn_view_history);
        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(navhost);

        ReceiverPhoneNumber = getArguments().getString("receiverPhoneNumber");
        Amount = getArguments().getString("amount");


        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body", "Hello, I just Sent "+ Amount + " Using" + " the Tingtel App");
                sendIntent.putExtra("address", ReceiverPhoneNumber);
                sendIntent.setType("vnd.android-dir/mms-sms");
                Objects.requireNonNull(getActivity()).startActivity(sendIntent);

            }
        });

        btnMakeAnotherTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_transferAirtimeSuccessFragment_to_mainFragment, null);
            }
        });

        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_transferAirtimeSuccessFragment_to_transactionHistoryFragment, null);
            }
        });

        return view;
    }

}
