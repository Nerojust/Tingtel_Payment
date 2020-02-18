package tingtel.payment.fragments.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import tingtel.payment.R;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.History;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

public class TransferAirtimePreviewFragment extends Fragment {

    String SenderSimNetwork;
    String SenderPhoneNumber;
    String ReceiverSimNetwork;
    String ReceiverPhoneNumber;
    String Pin;
    int SimNo;
    String Amount;
    String SimSerial;
    SessionManager sessionManager;

    String TingtelNumber;
    TextView tvSenderPhoneNumber;
    TextView tvReceiverPhoneNumber;
    TextView tvAmount;
    TextView tvServiceFee;
    TextView tvCreditedAmount;
    Button btnTransfer;
    Button btnBack;
    NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_transfer_airtime_preview, container, false);

        SenderSimNetwork = getArguments().getString("senderSimNetwork");
        ReceiverSimNetwork = getArguments().getString("receiverSimNetwork");
        SimNo = getArguments().getInt("simNo");
        Amount = getArguments().getString("amount");
        ReceiverPhoneNumber = getArguments().getString("receiverPhoneNumber");
        Pin = getArguments().getString("pin");

        sessionManager = AppUtils.getSessionManagerInstance();


        tvSenderPhoneNumber = view.findViewById(R.id.tv_sender_phone_number);
        tvReceiverPhoneNumber = view.findViewById(R.id.tv_receiver_phone_number);
        tvAmount = view.findViewById(R.id.tv_amount);
        tvServiceFee = view.findViewById(R.id.tv_service_fee);
        tvCreditedAmount = view.findViewById(R.id.tv_credited_amount);
        btnTransfer = view.findViewById(R.id.btn_transfer);
        btnBack = view.findViewById(R.id.btn_back);
        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(navhost);

        if (SimNo == 0) {
            SenderPhoneNumber = sessionManager.getSimPhoneNumber();
            SimSerial = sessionManager.getSimSerialICCID();
        } else if (SimNo == 1) {
            SenderPhoneNumber = sessionManager.getSimPhoneNumber1();
            SimSerial = sessionManager.getSimSerialICCID1();
        }


        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runAirtimeTransferUssd();

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_transferAirtimePreviewFragment_to_transferAirtimeReceiverInfoFragment, null);
            }
        });


        return view;
    }

    private void runAirtimeTransferUssd() {

        TingtelNumber = "08174612405";
        String UssdCode;

        if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("mtn")) {
           UssdCode = "*600*" + TingtelNumber + "*" + Amount + "*" + Pin + "#";
        } else if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("air")) {
                    sendSms();
                    return;
        } else if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("9mo")) {
            UssdCode = "*223*" + Pin + "*" + Amount + "*" + TingtelNumber + "#";
        } else if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("glo")) {
            UssdCode = "*131*" + TingtelNumber + "*" +  Amount + "*" + Pin + "#";

        } else {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            return;
        }

        saveHistory();

        Bundle bundle = new Bundle();
        bundle.putString("receiverPhoneNumber", ReceiverPhoneNumber);
        bundle.putString("amount", Amount);
        navController.navigate(R.id.action_transferAirtimePreviewFragment_to_transferAirtimeSuccessFragment, null);
    }

    private void saveHistory() {

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
/*
  in the background, save the data to roomdb using the balance model
 */
                Date currentDate = Calendar.getInstance().getTime();
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(getContext());

                //creating a task
                History history = new History();
               history.setSenderNetwork(SenderSimNetwork);
               history.setSenderPhoneNumber(SenderPhoneNumber);
               history.setReceiverNetwork(ReceiverSimNetwork);
               history.setReceiverPhoneNumber(ReceiverPhoneNumber);
               history.setSimSerial(SimSerial);
               history.setAmount(Amount);
               history.setDate(currentDate);


                //adding to database
                appdatabase.historyDao().insert(history);

                return null;
            }
        }
/*
  instantiate class and call execute
 */
        SaveTask st = new SaveTask();
        st.execute();




    }

    private void sendSms() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "2U " + TingtelNumber + " "+Amount + " "+ Pin);
        sendIntent.putExtra("address", "432");
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
    }


}
