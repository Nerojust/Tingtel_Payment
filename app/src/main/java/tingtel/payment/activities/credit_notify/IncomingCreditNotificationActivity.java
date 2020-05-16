package tingtel.payment.activities.credit_notify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.adapters.IncomingCreditAdapter;
import tingtel.payment.models.credit_notification.CreditNotificationResponse;
import tingtel.payment.models.credit_notification.CreditNotificationSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.CreditNotificationInterface;

public class IncomingCreditNotificationActivity extends AppCompatActivity {
    private ImageView homeImageview, settingsImagview, backButtonImageview;
    private RecyclerView recyclerView;
    private LinearLayout noRecordFoundLayout;
    private AlertDialog alertDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_credit_notifidation);

        initViews();
        initListeners();

        if (AppUtils.isNetworkAvailable(Objects.requireNonNull(this))) {
            getCreditHistory();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_network_available), Toast.LENGTH_LONG).show();
            finish();

        }
    }

    private void initViews() {
        backButtonImageview = findViewById(R.id.backArrowLayout);
        homeImageview = findViewById(R.id.homeImageview);
        settingsImagview = findViewById(R.id.settingsImageview);

        noRecordFoundLayout = findViewById(R.id.no_result_found_layout);
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setOnRefreshListener(this::getCreditHistory);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);

    }

    private void initListeners() {
        backButtonImageview.setOnClickListener(v -> {
            finish();
        });

        homeImageview.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        });

        settingsImagview.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void getCreditHistory() {
        swipeRefreshLayout.setRefreshing(true);

        CreditNotificationSendObject creditNotificationSendObject = new CreditNotificationSendObject();
        creditNotificationSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));
        creditNotificationSendObject.setPhone(AppUtils.getSessionManagerInstance().getNumberFromLogin());

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.getCreditNotification(creditNotificationSendObject, new CreditNotificationInterface() {
            @Override
            public void onSuccess(CreditNotificationResponse creditNotificationResponse) {

                if (creditNotificationResponse.getData().size() == 0) {
                    noRecordFoundLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    noRecordFoundLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    IncomingCreditAdapter incomingCreditAdapter = new IncomingCreditAdapter(IncomingCreditNotificationActivity.this, creditNotificationResponse.getData());
                    recyclerView.setAdapter(incomingCreditAdapter);
                    incomingCreditAdapter.notifyDataSetChanged();
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onError(String error) {
                AppUtils.showDialog(error, IncomingCreditNotificationActivity.this);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showDialog("" + errorCode, IncomingCreditNotificationActivity.this);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

}
