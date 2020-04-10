package tingtel.payment.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.sign_in.SignInActivity;
import tingtel.payment.adapters.SimCardsAdapter;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;
import tingtel.payment.models.login.CustomerLoginResponse;
import tingtel.payment.models.login.CustomerLoginSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.LoginResponseInterface;

public class ManageSimActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {
    List<SimCards> simCards = new ArrayList<>();
    AppDatabase appDatabase;
    RecyclerView recyclerView;
    SimCardsAdapter adapter;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sim);
        appDatabase = AppDatabase.getDatabaseInstance(this);
        sessionManager = AppUtils.getSessionManagerInstance();

        initViews();

        makeLoginRequestToFetchSimDetails();

        simCards = appDatabase.simCardsDao().getAllItems();

        adapter = new SimCardsAdapter(this, simCards, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }


    private void initViews() {
        recyclerView = findViewById(R.id.rv_simCards);
    }

    private void makeLoginRequestToFetchSimDetails() {

        CustomerLoginSendObject loginSendObject = new CustomerLoginSendObject();
        loginSendObject.setUsername(sessionManager.getUserame());
        loginSendObject.setPassword(sessionManager.getPassword());
        loginSendObject.setHash(AppUtils.getSHA512(sessionManager.getUserame() + sessionManager.getPassword() + BuildConfig.HASH_KEY));


        Gson gson = new Gson();
        String jsonObject = gson.toJson(loginSendObject);
        AppUtils.getSessionManagerInstance().setLoginJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.loginInUser(loginSendObject, new LoginResponseInterface() {
            @Override
            public void onSuccess(CustomerLoginResponse loginResponses) {

                appDatabase.simCardsDao().delete();

                if (loginResponses.getSim1().get(0).getPhone() != null) {
                    SimCards sim = new SimCards();
                    sim.setPhoneNumber(loginResponses.getSim1().get(0).getPhone());
                    sim.setSimSerial(loginResponses.getSim1().get(0).getSim1Serial());
                    sim.setSimNetwork(loginResponses.getSim1().get(0).getUserNetwork());
                    //adding to database
                    appDatabase.simCardsDao().insert(sim);
                    Log.e("TingtelApp", "number is" + loginResponses.getSim1().get(0).getPhone());
                }
                if (loginResponses.getSim2().get(0).getPhone2() != null) {
                    SimCards sim = new SimCards();
                    sim.setPhoneNumber(loginResponses.getSim2().get(0).getPhone2());
                    sim.setSimSerial(loginResponses.getSim2().get(0).getSim2Serial());
                    sim.setSimNetwork(loginResponses.getSim2().get(0).getSim2UserNetwork());
                    //adding to database
                    appDatabase.simCardsDao().insert(sim);
                    Log.e("TingtelApp", "number is" + loginResponses.getSim2().get(0).getPhone2());
                }


                if (loginResponses.getSim3().get(0).getPhone3() != null) {
                    SimCards sim = new SimCards();
                    sim.setPhoneNumber(loginResponses.getSim3().get(0).getPhone3());
                    sim.setSimSerial(loginResponses.getSim3().get(0).getSim3Serial());
                    sim.setSimNetwork(loginResponses.getSim3().get(0).getSim3UserNetwork());
                    //adding to database
                    appDatabase.simCardsDao().insert(sim);
                    Log.e("TingtelApp", "number is" + loginResponses.getSim3().get(0).getPhone3());
                }
                if (loginResponses.getSim4().get(0).getPhone4() != null) {
                    SimCards sim = new SimCards();
                    sim.setPhoneNumber(loginResponses.getSim4().get(0).getPhone4());
                    sim.setSimSerial(loginResponses.getSim4().get(0).getSim4Serial());
                    sim.setSimNetwork(loginResponses.getSim4().get(0).getSim4UserNetwork());
                    //adding to database
                    appDatabase.simCardsDao().insert(sim);
                    Log.e("TingtelApp", "number is" + loginResponses.getSim4().get(0).getPhone4());
                }


                AppUtils.dismissLoadingDialog();


            }

            @Override
            public void onError(String error) {
                if (error.equalsIgnoreCase("Invalid Hash Key"))
                    AppUtils.showSnackBar("Invalid credentials", recyclerView);
                else {
                    if (error.contains("failed to connect")) {
                        AppUtils.showSnackBar("Network error, please try again",   recyclerView);
                    } else {
                        AppUtils.showSnackBar(error, recyclerView);
                    }
                }

                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.dismissLoadingDialog();
                AppUtils.showSnackBar(String.valueOf(errorCode), recyclerView);
            }
        });



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
