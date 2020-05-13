package tingtel.payment.activities.settings;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.R;
import tingtel.payment.adapters.SimCardsAdapter;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;
import tingtel.payment.models.login.CustomerLoginResponse;
import tingtel.payment.models.login.CustomerLoginSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.utils.NetworkCarrierUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.LoginResponseInterface;

public class ManageSimActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {
    private List<SimCards> simCards = new ArrayList<>();
    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private SimCardsAdapter adapter;

    private LinearLayout noRecordFoundLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager sessionManager;
    private String sim2number;
    private String sim2number1;
    private String sim2network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sim);
        appDatabase = AppDatabase.getDatabaseInstance(this);
        sessionManager = AppUtils.getSessionManagerInstance();

        initViews();
        initRv();
        makeLoginRequestToFetchSimDetails();
    }

    private void initRv() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_simCards);
        noRecordFoundLayout = findViewById(R.id.no_result_found_layout);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this::makeLoginRequestToFetchSimDetails);
    }

    private void makeLoginRequestToFetchSimDetails() {
        AppUtils.initLoadingDialog(this);

        CustomerLoginSendObject loginSendObject = new CustomerLoginSendObject();
        loginSendObject.setUsername(sessionManager.getUserame());
        loginSendObject.setPassword(sessionManager.getPassword());
        loginSendObject.setHash(AppUtils.generateHash(sessionManager.getUserame(), sessionManager.getPassword()));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(loginSendObject);
        AppUtils.getSessionManagerInstance().setLoginJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.loginInUser(loginSendObject, new LoginResponseInterface() {
            @Override
            public void onSuccess(CustomerLoginResponse loginResponses) {
                recyclerView.removeAllViewsInLayout();
                appDatabase.simCardsDao().delete();

                saveNewDataIntoDatabase(loginResponses);

                simCards = appDatabase.simCardsDao().getAllItems();
                if (simCards.size() == 0) {
                    noRecordFoundLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                } else {
                    noRecordFoundLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    adapter = new SimCardsAdapter(ManageSimActivity.this, simCards, ManageSimActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onError(String error) {
                AppUtils.showDialog(error, ManageSimActivity.this);
                noRecordFoundLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.dismissLoadingDialog();
                noRecordFoundLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                AppUtils.showSnackBar(String.valueOf(errorCode), recyclerView);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void saveNewDataIntoDatabase(CustomerLoginResponse loginResponses) {
        if (loginResponses.getSim1().get(0).getPhone() != null) {
            SimCards sim = new SimCards();
            sim.setPhoneNumber(loginResponses.getSim1().get(0).getPhone());
            sim.setSimSerial(loginResponses.getSim1().get(0).getSim1Serial());
            sim.setSimNetwork(loginResponses.getSim1().get(0).getUserNetwork());
            //adding to database
            appDatabase.simCardsDao().insert(sim);
            Log.e("TingtelApp", "my number is" + loginResponses.getSim1().get(0).getPhone());
        }
        if (loginResponses.getSim2().get(0).getPhone2() != null) {
            SimCards sim = new SimCards();
            sim.setPhoneNumber(loginResponses.getSim2().get(0).getPhone2());
            sim.setSimSerial(loginResponses.getSim2().get(0).getSim2Serial());
            sim.setSimNetwork(loginResponses.getSim2().get(0).getSim2UserNetwork());
            //adding to database
            appDatabase.simCardsDao().insert(sim);
            Log.e("TingtelApp", "my number is" + loginResponses.getSim2().get(0).getPhone2());
        }
        if (loginResponses.getSim3().get(0).getPhone3() != null) {
            SimCards sim = new SimCards();
            sim.setPhoneNumber(loginResponses.getSim3().get(0).getPhone3());
            sim.setSimSerial(loginResponses.getSim3().get(0).getSim3Serial());
            sim.setSimNetwork(loginResponses.getSim3().get(0).getSim3UserNetwork());
            //adding to database
            appDatabase.simCardsDao().insert(sim);
            Log.e("TingtelApp", "my number is" + loginResponses.getSim3().get(0).getPhone3());
        }
        if (loginResponses.getSim4().get(0).getPhone4() != null) {
            SimCards sim = new SimCards();
            sim.setPhoneNumber(loginResponses.getSim4().get(0).getPhone4());
            sim.setSimSerial(loginResponses.getSim4().get(0).getSim4Serial());
            sim.setSimNetwork(loginResponses.getSim4().get(0).getSim4UserNetwork());
            //adding to database
            appDatabase.simCardsDao().insert(sim);
            Log.e("TingtelApp", "my number is" + loginResponses.getSim4().get(0).getPhone4());
        }


        updateSimDetails();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void updateSimDetails() {
        NetworkCarrierUtils.getCarrierOfSim(getApplicationContext(), ManageSimActivity.this);
        String NoOfSIm = sessionManager.getSimStatus();
        String Sim1Serial = sessionManager.getSimSerialICCID();
        String Sim2Serial = sessionManager.getSimSerialICCID1();

        switch (NoOfSIm) {
            case "NO SIM":
                break;
            case "SIM1":
                String sim1num;
                String sim1net;

                if (appDatabase.simCardsDao().getSerial(Sim1Serial).size() == 0) {
                    sim1num = "";
                    sim1net="";
                } else {
                    sim1num = appDatabase.simCardsDao().getSerial(Sim2Serial).get(0).getPhoneNumber();
                    sim1net = appDatabase.simCardsDao().getSerial(Sim2Serial).get(0).getSimNetwork();
                }

                sessionManager.setSimOnePhoneNumber(sim1num);
                sessionManager.setSimOneNetworkName(sim1net);
                sessionManager.setSimTwoPhoneNumber("");
                sessionManager.setSimTwoNetworkName("");

                break;
            case "SIM1 SIM2":
                String sim1number;
                String sim1network;

                if (appDatabase.simCardsDao().getSerial(Sim1Serial).size() == 0) {
                    sim1number = "";
                    sim1network="";
                } else {
                    sim1number = appDatabase.simCardsDao().getSerial(Sim1Serial).get(0).getPhoneNumber();
                    sim1network = appDatabase.simCardsDao().getSerial(Sim1Serial).get(0).getSimNetwork();
                }

                if (appDatabase.simCardsDao().getSerial(Sim2Serial).size() == 0) {
                    sim2number = "";
                    sim2network="";
                } else {
                    sim2number = appDatabase.simCardsDao().getSerial(Sim2Serial).get(0).getPhoneNumber();
                    sim2network = appDatabase.simCardsDao().getSerial(Sim2Serial).get(0).getSimNetwork();
                }

                sessionManager.setSimOnePhoneNumber(sim1number);
                sessionManager.setSimOneNetworkName(sim1network);
                sessionManager.setSimTwoPhoneNumber(sim2number);
                sessionManager.setSimTwoNetworkName(sim2network);
                break;
        }
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
