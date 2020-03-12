package tingtel.payment.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LifecycleObserver;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MyApplication extends Application implements LifecycleObserver {

    private static final String CUSTOMER_SESSION = "Tingtelpref";
    private static MyApplication myApplication;
    private String LOG_TAG = "ApplicationObserver";

    public static SharedPreferences getSharedPreferencesCustomer() {
        return myApplication.getSharedPreferences(CUSTOMER_SESSION, Context.MODE_PRIVATE);
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;


        /*OkHttpClient okHttpClientNetwork = new OkHttpClient().newBuilder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Request.Builder newRequest = request.newBuilder().header("authToken", "Bearer "
                            + sessionManagerAgent.getUserToken());
                    Response exp = chain.proceed(newRequest.build());
                    // 3. check the response: have we got a 401?
                    expired = exp.code() == HttpURLConnection.HTTP_UNAUTHORIZED;
                    unauthorized = exp.code() == HttpURLConnection.HTTP_FORBIDDEN;
                    //save the status for use
                    sessionManagerAgent.setIsTokenExpired(expired);
                    sessionManagerAgent.setIsForbidden(unauthorized);
                    return exp;
                })
                .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClientNetwork)
                .baseUrl(Constants.BASE_URL_VPOS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/

    }

    //using retrofit
//    public Retrofit getRetrofit() {
//        return retrofit;
//    }


    //inactivity logout settings
    public static class LogOutTimerUtil {

        // delay in milliseconds i.e. 5 min = 300000 ms
        private static Timer longTimer;
        private static int logoutTime;

        public static synchronized void startLogoutTimer(final Context context, LogOutListener logOutListener) {
            stopLogoutTimer();

            //logoutTime = sessionManagerAgent.getSessionIdleTimeForAgent() * 60000;
            //todo: change back
            logoutTime = 1000000;

            if (longTimer == null) {
                longTimer = new Timer();
                longTimer.schedule(new TimerTask() {
                    public void run() {
                        cancel();
                        longTimer = null;
                        try {
                            boolean foreGround = new LogOutTimerUtil.ForegroundCheckTask().execute(context).get();
                            if (foreGround) {
                                try {
                                    logOutListener.doLogout();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }, logoutTime);
            }
        }

        private static synchronized void stopLogoutTimer() {
            if (longTimer != null) {
                longTimer.cancel();
                longTimer = null;
            }
        }

        public interface LogOutListener {
            void doLogout();
        }

        static class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

            @Override
            protected Boolean doInBackground(Context... params) {
                final Context context = params[0].getApplicationContext();
                return isAppOnForeground(context);
            }

            private boolean isAppOnForeground(Context context) {
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                if (appProcesses == null) {
                    return false;
                }
                final String packageName = context.getPackageName();
                for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                    if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }


}
