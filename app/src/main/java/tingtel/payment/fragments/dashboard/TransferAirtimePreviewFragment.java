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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import tingtel.payment.R;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.History;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.DialUtils.dialUssdCode;

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
    ImageView imgSender;
    ImageView imgReceiver;
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

        imgSender = view.findViewById(R.id.img_sender);
        imgReceiver = view.findViewById(R.id.img_receiver);

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


        populateDetailsTextViews();



        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runAirtimeTransferUssd();

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
            }
        });


        return view;
    }

    private void populateDetailsTextViews() {

        tvSenderPhoneNumber.setText(SenderPhoneNumber);
        tvReceiverPhoneNumber.setText(ReceiverPhoneNumber);
        tvAmount.setText(Amount);
        setNetworkLogo();

    }

    private void setNetworkLogo() {

        if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("mtn")) {
            imgSender.setBackgroundResource(R.drawable.mtn_logo);
        } else if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("air")) {
            imgSender.setBackgroundResource(R.drawable.airtel_logo);
        }  else if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("glo")) {
            imgSender.setBackgroundResource(R.drawable.glo_logo);
        }  else if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("9mo")
        || (SenderSimNetwork.substring(0,3).equalsIgnoreCase("eti"))) {
            imgSender.setBackgroundResource(R.drawable.nmobile_logo);
        }



        if (ReceiverSimNetwork.substring(0,3).equalsIgnoreCase("mtn")) {
            imgReceiver.setBackgroundResource(R.drawable.mtn_logo);
        } else if (ReceiverSimNetwork.substring(0,3).equalsIgnoreCase("air")) {
            imgReceiver.setBackgroundResource(R.drawable.airtel_logo);
        }  else if (ReceiverSimNetwork.substring(0,3).equalsIgnoreCase("glo")) {
            imgReceiver.setBackgroundResource(R.drawable.glo_logo);
        }  else if (ReceiverSimNetwork.substring(0,3).equalsIgnoreCase("9mo")
                || (ReceiverSimNetwork.substring(0,3).equalsIgnoreCase("eti"))) {
            imgReceiver.setBackgroundResource(R.drawable.nmobile_logo);
        }

    }

    private void runAirtimeTransferUssd() {



        String UssdCode;

        if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("mtn")) {
            TingtelNumber = "08145995531";
           UssdCode = "*600*" + TingtelNumber + "*" + Amount + "*" + Pin + "#";

            dialUssdCode(
                    getActivity(),
                    UssdCode,
                    SimNo
            );

        } else if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("air")) {
            TingtelNumber = "08";
                    sendSms();
            UssdCode = "";
        } else if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("9mo")) {

            TingtelNumber = "08174612405";
            UssdCode = "*223*" + Pin + "*" + Amount + "*" + TingtelNumber + "#";
            dialUssdCode(
                    getActivity(),
                    UssdCode,
                    SimNo
            );

        } else if (SenderSimNetwork.substring(0,3).equalsIgnoreCase("glo")) {

            TingtelNumber = "08174612405";
            UssdCode = "*131*" + TingtelNumber + "*" +  Amount + "*" + Pin + "#";
            dialUssdCode(
                    getActivity(),
                    UssdCode,
                    SimNo
            );


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
