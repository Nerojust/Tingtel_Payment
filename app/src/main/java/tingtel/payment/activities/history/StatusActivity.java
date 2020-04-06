package tingtel.payment.activities.history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.VerticalStepView;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.ReportIssueActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.models.transaction_status.CheckTransactionStatusResponse;
import tingtel.payment.models.transaction_status.CheckTransactionStatusSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.CheckTransactionStatusInterface;

public class StatusActivity extends AppCompatActivity {
    private static final String TAG = "Status Activity";
    private VerticalStepView stepView;
    private ImageView backButtonImageview, homeImageview, settingsImageview;
    private TextView complaintTextview, referenceIdTextview;
    private String simNumber;
    private SessionManager sessionManager;
    private String phoneNumber;
    private String amount;
    private Button refreshButton;
    private String sender_number;
    private String receiver_number;
    private String referenceId;
    private String created_date;
    private List<String> list0;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        sessionManager = AppUtils.getSessionManagerInstance();
        list0 = new ArrayList<>();

        initViews();
        initListeners();
        getExtras();
        checkStatusOfTransaction();
    }

    private void getExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            amount = intent.getStringExtra("amount");
            referenceId = intent.getStringExtra("ref_id");
            created_date = intent.getStringExtra("date");
            status = intent.getIntExtra("status", 0);
            sender_number = intent.getStringExtra("sender_number");
            receiver_number = intent.getStringExtra("receiver_number");

            sender_number = AppUtils.checkPhoneNumberAndRemovePrefix(sender_number);
            receiver_number = AppUtils.checkPhoneNumberAndRemovePrefix(receiver_number);
        }
    }


    private void initListeners() {
        backButtonImageview.setOnClickListener(v -> finish());

        homeImageview.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finishAffinity();

        });

        settingsImageview.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        refreshButton.setOnClickListener(v -> {
            recreate();
        });

        complaintTextview.setOnClickListener(v -> startActivity(new Intent(this, ReportIssueActivity.class)));
    }

    @Override
    public void recreate() {
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
        super.recreate();
    }

    private void initViews() {
        backButtonImageview = findViewById(R.id.backArrowLayout);
        homeImageview = findViewById(R.id.homeImageview);
        settingsImageview = findViewById(R.id.settingsImageview);
        complaintTextview = findViewById(R.id.complaintTextview);
        referenceIdTextview = findViewById(R.id.referenceIdTextview);
        refreshButton = findViewById(R.id.refreshButton);

        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, hh:mm a");
        stepView = findViewById(R.id.step_view);
    }

    private void setStepViewToPendingStatusHistory() {
        list0.add(getResources().getString(R.string.naira) + amount + " from " + sender_number + "(You)");
        list0.add("TO TINGTEL \n at " + created_date);
        list0.add("From TINGTEL");
        list0.add("To " + receiver_number);
        setStepParams(list0, list0.size() - 2);
    }

    private void setStepViewToCompletedStatusHistory() {
        list0.add(getResources().getString(R.string.naira) + amount + " from " + sender_number + "(You)");
        list0.add("TO TINGTEL \n at " + created_date);
        list0.add("From TINGTEL");
        list0.add("To " + receiver_number);
        setStepParams(list0, list0.size());
    }

    private void setStepParams(List<String> list0, int size) {
        stepView.setStepsViewIndicatorComplectingPosition(size)
                .reverseDraw(false)//default is true
                .setStepViewTexts(list0)
                .setLinePaddingProportion(1f)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, R.color.tingtel_red_color))
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.black))
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, R.color.black))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.tingtel_red_color))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.ic_check_circle))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.attention));
    }


    private void checkStatusOfTransaction() {
        AppUtils.initLoadingDialog(this);

        CheckTransactionStatusSendObject checkTransactionStatusSendObject = new CheckTransactionStatusSendObject();
        checkTransactionStatusSendObject.setRef(referenceId);
        checkTransactionStatusSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.checkTransactionStatus(checkTransactionStatusSendObject, new CheckTransactionStatusInterface() {
            @Override
            public void onSuccess(CheckTransactionStatusResponse sendOTPresponse) {

                if (sendOTPresponse.getTransactions() != null) {
                    Integer status = sendOTPresponse.getTransactions().getStatus();
                    referenceIdTextview.setText(referenceId);
                    if (status == 0) {
                        setStepViewToPendingStatusHistory();
                    } else {
                        setStepViewToCompletedStatusHistory();
                    }

                } else {
                    AppUtils.showDialog("No transactions found", StatusActivity.this);
                }
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onError(String error) {
                try {
                    displayDialog(error, StatusActivity.this);
                } catch (Exception e) {
                    Log.e(TAG, "onError: " + e.getMessage());
                }
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showSnackBar(String.valueOf(errorCode), complaintTextview);
                AppUtils.dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AppUtils.initLoadingDialog(this);
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
        AlertDialog alertDialog = builder.create();

        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        Button retry = dialogView.findViewById(R.id.btn_ok);

        tvMessage.setText(message);
        retry.setOnClickListener(v -> {
            checkStatusOfTransaction();
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fade_out);
    }
}
