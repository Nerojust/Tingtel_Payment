package tingtel.payment.fragments.dashboard;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;
import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.activities.sign_up.SignUpActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.credit_notification.CreditNotificationResponse;
import tingtel.payment.models.credit_notification.CreditNotificationSendObject;
import tingtel.payment.models.credit_notification.Data;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.CreditNotificationInterface;


public class DashboardFragment extends Fragment {

    private NavController navController;
    private AppDatabase appDatabase;
    private SessionManager sessionManager;
    private ImageView settingsImagview;
    private Button btnTransferAirtime, btnHistory;
    private TextView notificationCountTextview;
    private int databaseRecords;
    private AppDatabase appDatabase1;
    private int recordsFromServer;
    private int amountToDisplay;
    private LinearLayout notificationLayout;


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
        appDatabase = AppDatabase.getDatabaseInstance(requireContext());
        Fragment navhost = requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }

    private void initViews(View view) {
        MobileAds.initialize(getContext(), initializationStatus -> {
        });

        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        appDatabase1 = AppDatabase.getDatabaseInstance(requireActivity());
        notificationLayout = view.findViewById(R.id.notificationLayout);
        notificationCountTextview = view.findViewById(R.id.number);
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
        getCreditHistory();
    }

    private void initListeners() {
        notificationLayout.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_incomingCreditNotifidationActivity));
        settingsImagview.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));

        btnTransferAirtime.setOnClickListener(v -> {
            if (sessionManager.getSerialsDontMatchAnyOnDevice()) {
                checkIfSimIsRegistered();
            }
            navController.navigate(R.id.action_mainFragment_to_transferAirtimeActivity, null);
        });

        btnHistory.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_historyActivity, null));
    }

    private void getCreditHistory() {
        CreditNotificationSendObject creditNotificationSendObject = new CreditNotificationSendObject();
        creditNotificationSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));
        creditNotificationSendObject.setPhone(AppUtils.getSessionManagerInstance().getNumberFromLogin());

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.getCreditNotification(creditNotificationSendObject, new CreditNotificationInterface() {
            @Override
            public void onSuccess(CreditNotificationResponse creditNotificationResponse) {
                if (creditNotificationResponse.getData().size() != 0) {
                    List<Data> creditNotifications = appDatabase.creditHistoryDao().getAllCreditNotifications();
                    recordsFromServer = creditNotificationResponse.getData().size();
                    databaseRecords = creditNotifications.size();

                    if (recordsFromServer > databaseRecords) {
                        //we have new records to display
                        amountToDisplay = recordsFromServer - databaseRecords;
                        notificationCountTextview.setVisibility(View.VISIBLE);
                        notificationCountTextview.setText(String.valueOf(amountToDisplay));

                        for (int i = 0; i < creditNotificationResponse.getData().size(); i++) {
                            String fromNumber = creditNotificationResponse.getData().get(i).getSender();
                            String amount = creditNotificationResponse.getData().get(i).getAmount();
                            String toNumber = creditNotificationResponse.getData().get(i).getPhone();
                            String username = creditNotificationResponse.getData().get(i).getName();
                            int sentStatus = creditNotificationResponse.getData().get(i).getSent();
                            String dateSent = creditNotificationResponse.getData().get(i).getCreatedAt();
                            String txnId = creditNotificationResponse.getData().get(i).getTransId();

                            saveCreditHistoryToDatabase(username, fromNumber, toNumber, sentStatus, amount, txnId, dateSent);
                        }
                    } else {
                        //we dont have any
                        //set view to gone/invisible
                        notificationCountTextview.setVisibility(View.INVISIBLE);
                    }


                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onErrorCode(int errorCode) {

            }
        });
    }

    private void saveCreditHistoryToDatabase(String usernameOfSender, String senderNumber, String receiverNumber, int sentStatus,
                                             String amount, String trans_id, String dateCreated) {

        @SuppressLint("StaticFieldLeak")
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                // in the background, save the data to roomdb using the credit data model
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(requireContext());

                Data creditNotification = new Data();
                creditNotification.setAmount(amount);
                creditNotification.setCreatedAt(dateCreated);
                creditNotification.setName(usernameOfSender);
                creditNotification.setPhone(receiverNumber);
                creditNotification.setSender(senderNumber);
                creditNotification.setSent(sentStatus);
                creditNotification.setTransId(trans_id);

                //adding to database
                appdatabase.creditHistoryDao().insert(creditNotification);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Toast.makeText(MainActivity.this, getResources().getString(R.string.beneficiary_saved), Toast.LENGTH_SHORT).show();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
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
