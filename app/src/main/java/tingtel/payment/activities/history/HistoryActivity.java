package tingtel.payment.activities.history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.adapters.HistoryAdapter;
import tingtel.payment.models.transaction_history.TransactionHistoryResponse;
import tingtel.payment.models.transaction_history.TransactionHistorySendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.TransactionHistoryInterface;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noRecordFound;
    private AlertDialog alertDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        noRecordFound = findViewById(R.id.no_result_found);
        recyclerView = findViewById(R.id.rv_history);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this::getAllHistory);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if (AppUtils.isNetworkAvailable(this)) {
            getAllHistory();

        } else {
            AppUtils.showSnackBar("No network available", recyclerView);
        }
    }

    private void getAllHistory() {
        AppUtils.initLoadingDialog(this);

        TransactionHistorySendObject transactionHistorySendObject = new TransactionHistorySendObject();
        transactionHistorySendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));
        transactionHistorySendObject.setUserPhone(AppUtils.getSessionManagerInstance().getNumberFromLogin());
        //Toast.makeText(this, AppUtils.getSessionManagerInstance().getNumberFromLogin(), Toast.LENGTH_SHORT).show();

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.getTransactionHistory(transactionHistorySendObject, new TransactionHistoryInterface() {
            @Override
            public void onSuccess(TransactionHistoryResponse transactionHistoryResponse) {
                if (transactionHistoryResponse != null) {
                    if (transactionHistoryResponse.getPhone1Transactions().size() == 0) {
                        noRecordFound.setVisibility(View.VISIBLE);
                        alertDialog.dismiss();
                    } else {
                        noRecordFound.setVisibility(View.GONE);
                        HistoryAdapter historyAdapter = new HistoryAdapter(HistoryActivity.this, transactionHistoryResponse);
                        recyclerView.setAdapter(historyAdapter);
                        historyAdapter.notifyDataSetChanged();
                        recyclerView.setHasFixedSize(true);
                        recyclerView.smoothScrollToPosition(0);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }else {
                    AppUtils.showDialog("Server Error", HistoryActivity.this);
                }
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onError(String error) {
                if (error.equalsIgnoreCase("Error retrieving data")) {
                    noRecordFound.setVisibility(View.VISIBLE);
                } else {
                    displayDialog(error, HistoryActivity.this);
                    noRecordFound.setVisibility(View.GONE);
                }
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showDialog(String.valueOf(errorCode), HistoryActivity.this);
                AppUtils.dismissLoadingDialog();
                noRecordFound.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        boolean status = AppUtils.getSessionManagerInstance().getComingFromSuccess();
        if (status) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        super.onBackPressed();

    }

    /**
     * to display a dialog
     *
     * @param message:  message to be displayed
     * @param activity: Get the calling activity
     */
    private void displayDialog(String message, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_retry, viewGroup, false);
        builder.setView(dialogView);
        alertDialog = builder.create();

        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        Button retry = dialogView.findViewById(R.id.btn_ok);

        tvMessage.setText(message);
        retry.setOnClickListener(v -> {
            getAllHistory();
            alertDialog.dismiss();
        });
        alertDialog.show();
    }
}
