package tingtel.payment.web_services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import tingtel.payment.BuildConfig;
import tingtel.payment.models.credit_notification.CreditNotificationResponse;
import tingtel.payment.models.credit_notification.CreditNotificationSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.Constants;
import tingtel.payment.web_services.interfaces.CreditNotificationInterface;

/**
 * this service handles background async call to get credit notification
 */
public class NotificationService extends Service {

    // default interval for syncing data
    public static final long DEFAULT_SYNC_INTERVAL = 30 * 1000;
    private Handler mHandler;
    // task to be run here
    private Runnable runnableService = new Runnable() {
        @Override
        public void run() {
            syncData();
            // Repeat this runnable code block again every ... min
            mHandler.postDelayed(runnableService, Constants.DEFAULT_SYNC_INTERVAL);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create the Handler object
        mHandler = new Handler();
        // Execute a runnable task as soon as possible
        mHandler.post(runnableService);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private synchronized void syncData() {
        CreditNotificationSendObject creditNotificationSendObject = new CreditNotificationSendObject();
        creditNotificationSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));
        creditNotificationSendObject.setPhone(AppUtils.getSessionManagerInstance().getNumberFromLogin());

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.getCreditNotification(creditNotificationSendObject, new CreditNotificationInterface() {
            @Override
            public void onSuccess(CreditNotificationResponse creditNotificationResponse) {
                if (creditNotificationResponse.getData().size() != 0) {
                    Toast.makeText(NotificationService.this, creditNotificationResponse.getData().size(), Toast.LENGTH_SHORT).show();
//                    String fromNumber = creditNotificationResponse.getSender();
//                    String amount = creditNotificationResponse.getAmount();
//                    String toNumber = creditNotificationResponse.getPhone();
//                    Log.e(TAG, "onSuccess: " + fromNumber + " " + toNumber + " " + amount);
//                    Toast.makeText(NotificationService.this, fromNumber + " " + toNumber + " " + amount, Toast.LENGTH_SHORT).show();
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
}