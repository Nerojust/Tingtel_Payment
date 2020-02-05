package tingtel.payment.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MyApplication extends Application {

    private static MyApplication myApplication;
    private static final String CUSTOMER_SESSION = "Tingtelpref";

    public static SharedPreferences getSharedPreferencesCustomer() {
        return myApplication.getSharedPreferences(CUSTOMER_SESSION, Context.MODE_PRIVATE);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

       // ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }
}
