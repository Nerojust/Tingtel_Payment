package tingtel.payment.fragments.transfer_airtime;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.gson.Gson;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.History;
import tingtel.payment.models.send_credit.SendCreditDetailsResponse;
import tingtel.payment.models.send_credit.SendCreditDetailsSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.Constants;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.utils.TingtelObserver;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.SendCreditDetailsInterface;

import static tingtel.payment.utils.DialUtils.dialUssdCode;

public class TransferAirtimePreviewFragment extends Fragment {

    private Boolean buttonClicked = false;
    private Button btnTransfer;
    private String SenderSimNetwork;
    private String SenderPhoneNumber;
    private String ReceiverSimNetwork;
    private String ReceiverPhoneNumber;
    private String Pin;
    private int SimNo;
    private int called = 0;
    private int called1 = 0;
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
    private NavController navController;
    private ImageView homeImageview, settingsImagview, backButtonImageview;
    private boolean paused;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer_airtime_preview, container, false);

        getLifecycle().addObserver(new TingtelObserver());

        initViews(view);
        getExtrasFromIntent();

        if (SimNo == 0) {
            SenderPhoneNumber = sessionManager.getSimPhoneNumber();
            SimSerial = sessionManager.getSimSerialICCID();
        } else if (SimNo == 1) {
            SenderPhoneNumber = sessionManager.getSimPhoneNumber1();
            SimSerial = sessionManager.getSimSerialICCID1();
        }


        populateDetailsTextViews();
        initListeners();

        return view;
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

        btnTransfer.setOnClickListener(v -> {
           if (btnTransfer.getText().toString().equalsIgnoreCase("transfer")) {
               runAirtimeTransferUssd();
           }else if (btnTransfer.getText().toString().equalsIgnoreCase("verify")){
               sendDetailsToServerToCredit();
           }
        });
    }


    /**
     * instantiate all necessary views
     *
     * @param view
     */
    private void initViews(View view) {
        String[] descriptionData = {"Sender", "Receiver", "Summary", "Status"};
        StateProgressBar stateProgressBar = view.findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateDescriptionTypeface("font/rubik_regular.ttf");
        stateProgressBar.setStateNumberTypeface("font/rubik_regular.ttf");

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

        sessionManager.setReceiverPhoneNumber(ReceiverPhoneNumber);
        sessionManager.setAmount(Amount);
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
            TingtelNumber = Constants.TINGTEL_MTN;
            UssdCode = "*600*" + TingtelNumber + "*" + Amount + "*" + Pin + "#";
            //Toast.makeText(getActivity(), UssdCode, Toast.LENGTH_LONG).show();
            dialUssdCode(
                    getActivity(),
                    UssdCode,
                    SimNo
            );

        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("air")) {
            TingtelNumber = Constants.TINGTEL_AIRTEL;

            sendSms();
            Toast.makeText(getContext(), "Please click on send message icon to complete the process", Toast.LENGTH_SHORT).show();
            UssdCode = "";
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("9mo")) {

            TingtelNumber = Constants.TINGTEL_9MOBILE;
            UssdCode = "*223*" + Pin + "*" + Amount + "*" + TingtelNumber + "#";
            dialUssdCode(
                    getActivity(),
                    UssdCode,
                    SimNo
            );

        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("glo")) {
            TingtelNumber = Constants.TINGTEL_GLO;
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

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
        new Handler().postDelayed(() -> {
            if (buttonClicked) {
                if (paused) {
                    btnTransfer.setText("Verify");
                    btnTransfer.setBackgroundColor(getResources().getColor(R.color.foreground_color));
                }
            }
        }, 2000);
    }


    private void sendDetailsToServerToCredit() {
        AppUtils.initLoadingDialog(getContext());

        SendCreditDetailsSendObject sendCreditDetailsSendObject = new SendCreditDetailsSendObject();
        sendCreditDetailsSendObject.setAmount(Amount);
        sendCreditDetailsSendObject.setBeneficiaryMsisdn(ReceiverPhoneNumber);
        sendCreditDetailsSendObject.setBeneficiaryNetwork(ReceiverSimNetwork);
        sendCreditDetailsSendObject.setSourceNetwork(SenderSimNetwork);
        sendCreditDetailsSendObject.setUserPhone(SenderPhoneNumber);
        String random = AppUtils.generateRandomString();
        sendCreditDetailsSendObject.setRef(random);
        sendCreditDetailsSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

        Gson gson = new Gson();
        String object = gson.toJson(sendCreditDetailsSendObject);
        sessionManager.setCreditRequestJsonObject(object);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.sendCreditDetailsToServer(sendCreditDetailsSendObject, new SendCreditDetailsInterface() {
            @Override
            public void onSuccess(SendCreditDetailsResponse changeEmailResponse) {
                Bundle bundle = new Bundle();
                bundle.putString("senderSimNetwork", SenderSimNetwork);
                bundle.putString("receiverSimNetwork", ReceiverSimNetwork);
                bundle.putString("simSerial", SimSerial);
                bundle.putInt("simNo", SimNo);
                bundle.putString("amount", Amount);
                bundle.putString("receiverPhoneNumber", ReceiverPhoneNumber);

                AppUtils.dismissLoadingDialog();

                navController.navigate(R.id.action_transferAirtimePreviewFragment2_to_transferAirtimeSuccessFragment, bundle);

            }

            @Override
            public void onError(String error) {
                AppUtils.showDialog(error, getActivity());
                //retry
                //sendDetailsToServerToCredit();
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showDialog(String.valueOf(errorCode), getActivity());
                AppUtils.dismissLoadingDialog();
            }
        });
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
}
