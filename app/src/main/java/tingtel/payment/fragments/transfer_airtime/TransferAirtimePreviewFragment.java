package tingtel.payment.fragments.transfer_airtime;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.History;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.utils.TingtelObserver;

import static tingtel.payment.utils.DialUtils.dialUssdCode;

public class TransferAirtimePreviewFragment extends Fragment {

    Boolean buttonClicked;
    int buttonClickedInt = 0;
    private String SenderSimNetwork;
    private String SenderPhoneNumber;
    private String ReceiverSimNetwork;
    private String ReceiverPhoneNumber;
    private String Pin;
    private int SimNo;
    private String final_Amount;
    private String Amount;
    private String SimSerial;
    private SessionManager sessionManager;
    private String TingtelNumber;
    private TextView tvSenderPhoneNumber;
    private TextView tvReceiverPhoneNumber;
    private TextView tvAmount;
    private TextView tvServiceFee;
    private TextView tvCreditedAmount;
    private ImageView imgSender;
    private ImageView imgReceiver;
    private Button btnTransfer;
    private Button btnBack;
    private Button btnSendMessage;
    private Button btnCancel;
    private Button btnSaveBeneficiary;
    private EditText edMessage;
    private NavController navController;
    private ImageView homeImageview, settingsImagview,backButtonImageview;
    private LinearLayout layoutSuccess;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer_airtime_preview, container, false);

        getLifecycle().addObserver(new TingtelObserver());

        getExtrasFromIntent();
        initViews(view);

        if (SimNo == 0) {
            SenderPhoneNumber = sessionManager.getSimPhoneNumber();
            SimSerial = sessionManager.getSimSerialICCID();
        } else if (SimNo == 1) {
            SenderPhoneNumber = sessionManager.getSimPhoneNumber1();
            SimSerial = sessionManager.getSimSerialICCID1();
        }

        layoutSuccess.setVisibility(View.GONE);

        populateDetailsTextViews();
        initListeners();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        buttonClicked = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        buttonClickedInt += 1;

        Log.e("tingteltest", "i am here");
        if (buttonClickedInt > 3) {
            layoutSuccess.setVisibility(View.VISIBLE);
            //btnSaveBeneficiary.setEnabled(true);
            edMessage.setText("Hello, I Just transferred " + getResources().getString(R.string.naira) + final_Amount + " airtime to you using\n" +
                    "Tingtelpay. You can download the Tingtelpay app using the link\n https://play.google.com/store/apps/details?id=tingtel.payments");

            buttonClickedInt = 1;
        }

        if (buttonClickedInt == 3) {
            checkBalance();
        }

    }

    private void checkBalance() {
        String UssdCode;
        if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("mtn")) {
            UssdCode = "*556#";
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("air")) {
            UssdCode = "*123#";
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("glo")) {
            UssdCode = "*124*1#";
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("9mo") ||
                (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("eti"))) {
            UssdCode = "*232#";
        } else {

            Toast.makeText(getActivity(), "Cant Check USSD Balance for this network", Toast.LENGTH_LONG).show();
            return;
        }

        AppUtils.dialUssdCode(getActivity(), UssdCode, SimNo);
    }

    /**
     * instantiate listeners for click events.
     */
    private void initListeners() {

        backButtonImageview.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        homeImageview.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();

        });

        settingsImagview.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));

        btnTransfer.setOnClickListener(v -> runAirtimeTransferUssd());

        btnSendMessage.setOnClickListener(v -> shareViaSocial(edMessage.getText().toString()));

        btnCancel.setOnClickListener(v -> layoutSuccess.setVisibility(View.GONE));

        btnSaveBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveBeneficiarySheetFragment bottomSheetFragment = new SaveBeneficiarySheetFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ReceiverPhoneNumber", ReceiverPhoneNumber);
                bundle.putString("ReceiverNetwork", ReceiverSimNetwork);
                bottomSheetFragment.setArguments(bundle);
                bottomSheetFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }


    /**
     * instantiate all necessary views
     *
     * @param view
     */
    private void initViews(View view) {
        sessionManager = AppUtils.getSessionManagerInstance();
        backButtonImageview = view.findViewById(R.id.backArrowLayout);
        homeImageview = view.findViewById(R.id.homeImageview);
        settingsImagview = view.findViewById(R.id.settingsImageview);
        imgSender = view.findViewById(R.id.senderImage);
        imgReceiver = view.findViewById(R.id.receiverImage);

        tvSenderPhoneNumber = view.findViewById(R.id.tv_sender_phone_number);
        tvReceiverPhoneNumber = view.findViewById(R.id.tv_receiver_phone_number);
        tvAmount = view.findViewById(R.id.tv_amount);
        tvServiceFee = view.findViewById(R.id.tv_service_fee);
        tvCreditedAmount = view.findViewById(R.id.tv_credited_amount);
        btnTransfer = view.findViewById(R.id.btn_transfer);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSendMessage = view.findViewById(R.id.btn_send_message);
        btnSaveBeneficiary = view.findViewById(R.id.btn_save_beneficiary);

        edMessage = view.findViewById(R.id.ed_message);

        layoutSuccess = view.findViewById(R.id.layout_success);

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }

    /**
     * get detail from intent
     */
    private void getExtrasFromIntent() {
        SenderSimNetwork = Objects.requireNonNull(getArguments()).getString("senderSimNetwork");
        ReceiverSimNetwork = getArguments().getString("receiverSimNetwork");
        SimNo = getArguments().getInt("simNo");
        Amount = getArguments().getString("amount");
        ReceiverPhoneNumber = getArguments().getString("receiverPhoneNumber");
        Pin = getArguments().getString("pin");
    }

    /**
     * set data to views
     */
    private void populateDetailsTextViews() {
        tvSenderPhoneNumber.setText(SenderPhoneNumber);
        tvReceiverPhoneNumber.setText(ReceiverPhoneNumber);
        //calculate 10% of amount entered
        double dividedAmount = Integer.parseInt(Amount) / 10;
        double balanceAmount = Integer.parseInt(Amount) - dividedAmount;

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String finalDividedAmount = formatter.format(dividedAmount);
        String finalAmount = formatter.format(Double.parseDouble(Amount));
        String finalBalanceAmount = formatter.format(balanceAmount);

        tvAmount.setText(finalAmount);
        tvServiceFee.setText(finalDividedAmount);
        tvCreditedAmount.setText(finalBalanceAmount);

        final_Amount = finalBalanceAmount;

        setNetworkLogo();
    }

    /**
     * set logo images based on specific network detected.
     */
    private void setNetworkLogo() {
        if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("mtn")) {
            imgSender.setBackgroundResource(R.drawable.mtn_logo);
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("air")) {
            imgSender.setBackgroundResource(R.drawable.airtel_logo);
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("glo")) {
            imgSender.setBackgroundResource(R.drawable.glo_logo);
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("9mo")
                || (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("eti"))) {
            imgSender.setBackgroundResource(R.drawable.nmobile_logo);
        }


        if (ReceiverSimNetwork.substring(0, 3).equalsIgnoreCase("mtn")) {
            imgReceiver.setBackgroundResource(R.drawable.mtn_logo);
        } else if (ReceiverSimNetwork.substring(0, 3).equalsIgnoreCase("air")) {
            imgReceiver.setBackgroundResource(R.drawable.airtel_logo);
        } else if (ReceiverSimNetwork.substring(0, 3).equalsIgnoreCase("glo")) {
            imgReceiver.setBackgroundResource(R.drawable.glo_logo);
        } else if (ReceiverSimNetwork.substring(0, 3).equalsIgnoreCase("9mo")
                || (ReceiverSimNetwork.substring(0, 3).equalsIgnoreCase("eti"))) {
            imgReceiver.setBackgroundResource(R.drawable.nmobile_logo);
        }
    }

    /**
     * perform the ussd code run request
     */
    private void runAirtimeTransferUssd() {
        String UssdCode;

        if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("mtn")) {
            TingtelNumber = "08145995531";
            UssdCode = "*600*" + TingtelNumber + "*" + Amount + "*" + Pin + "#";
            //Toast.makeText(getActivity(), UssdCode, Toast.LENGTH_LONG).show();
            dialUssdCode(
                    getActivity(),
                    UssdCode,
                    SimNo
            );

        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("air")) {
            TingtelNumber = "08087000909";

            sendSms();

            UssdCode = "";
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("9mo")) {

            TingtelNumber = "08174612405";
            UssdCode = "*223*" + Pin + "*" + Amount + "*" + TingtelNumber + "#";
            dialUssdCode(
                    getActivity(),
                    UssdCode,
                    SimNo
            );

        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("glo")) {

            TingtelNumber = "08174612405";
            UssdCode = "*131*" + TingtelNumber + "*" + Amount + "*" + Pin + "#";
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

        buttonClicked = true;
    }

    /**
     * save the details to database.
     */
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

    /**
     * send the sms to user
     */
    private void sendSms() {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.addCategory(Intent.CATEGORY_DEFAULT);
        sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.setData(Uri.parse("sms:" + "432"));
        sendIntent.putExtra("sms_body", "2U " + TingtelNumber + " " + Amount + " " + Pin);

        startActivity(sendIntent);

    }

    public void shareViaSocial(String body) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent.setType("text/plain");
        txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Airtime Transfer Notification");
        txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(txtIntent, "Share"));
    }
}
