package tingtel.payment.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.sign_in.SignInActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.credit_notification.CreditNotificationResponse;
import tingtel.payment.models.credit_notification.CreditNotificationSendObject;
import tingtel.payment.models.credit_notification.Data;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.CreditNotificationInterface;

public class MainActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = AppUtils.getSessionManagerInstance();

        if (!sessionManager.getSavedCreditRecords()) {
            getCreditHistory();
        }
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
                }
                sessionManager.setSavedCreditRecords(true);
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
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(getApplicationContext());

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


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.do_you_want_to_logout))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, id) -> {
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppUtils.getSessionManagerInstance().logout();
                    startActivity(intent);
                    MainActivity.this.onSuperBackPressed();
                })
                .setNegativeButton(getResources().getString(R.string.no), (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fade_out);
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void doLogout() {
        new Handler(Looper.getMainLooper()).post(() -> AppUtils.logOutInactivitySessionTimeout(this));
    }
}
