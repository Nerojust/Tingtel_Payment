package tingtel.payment.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class MyApplication extends Application implements LifecycleObserver {

    private static MyApplication myApplication;
    private static final String CUSTOMER_SESSION = "Tingtelpref";
    private String LOG_TAG = "ApplicationObserver";

    public static SharedPreferences getSharedPreferencesCustomer() {
        return myApplication.getSharedPreferences(CUSTOMER_SESSION, Context.MODE_PRIVATE);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;


    }



}
