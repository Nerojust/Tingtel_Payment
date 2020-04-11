package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
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
    private List<SimCards> simCards = new ArrayList<>();
    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private SimCardsAdapter adapter;

    private LinearLayout noRecordFoundLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager sessionManager;

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
        recyclerView.setAdapter(adapter);
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
        loginSendObject.setHash(AppUtils.getSHA512(sessionManager.getUserame() + sessionManager.getPassword() + BuildConfig.HASH_KEY));


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
