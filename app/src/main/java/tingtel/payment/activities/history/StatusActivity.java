package tingtel.payment.activities.history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        sessionManager = AppUtils.getSessionManagerInstance();
        initViews();
        initListeners();

        if (sessionManager.getComingFromSuccess()) {
            if (sessionManager.getTransactionReference() != null) {
                referenceId = sessionManager.getTransactionReference();
                referenceIdTextview.setText(referenceId);
            }
            int clicked = sessionManager.getWhichSimWasClicked();
            if (clicked == 0) {
                phoneNumber = sessionManager.getSimPhoneNumber();
            } else {
                phoneNumber = sessionManager.getSimPhoneNumber1();
            }
            checkStatusOfTransaction();
        } else {
            Intent intent = getIntent();
            if (intent.getExtras() != null) {
                amount = intent.getStringExtra("amount");
                referenceId = intent.getStringExtra("ref_id");

                int status = intent.getIntExtra("status", 0);
                sender_number = intent.getStringExtra("sender_number");
                receiver_number = intent.getStringExtra("receiver_number");

                referenceIdTextview.setText(referenceId);
                if (status == 0) {
                    setStepViewToPendingStatusHistory();
                } else if (status == 1) {
                    setStepViewToCompletedStatusHistory();
                }
            }
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

        refreshButton.setOnClickListener(v -> checkStatusOfTransaction());

        complaintTextview.setOnClickListener(v -> startActivity(new Intent(this, ReportIssueActivity.class)));
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
        List<String> list0 = new ArrayList<>();
        list0.add(getResources().getString(R.string.naira) + amount + " from " + sender_number + "(You)");
        list0.add("TO TINGTEL \n at " + "4:23pm, Fri. 23rd March 2020");
        list0.add("From TINGTEL");
        list0.add("To " + receiver_number);
        stepView.setStepsViewIndicatorComplectingPosition(list0.size() - 2)
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

        stepView.setOnClickListener(v -> {
            Toast.makeText(this, "This is the current status of your transaction", Toast.LENGTH_SHORT).show();
        });
    }

    private void setStepViewToCompletedStatusHistory() {
        List<String> list0 = new ArrayList<>();
        list0.add(getResources().getString(R.string.naira) + amount + " from " + sender_number + "(You)");
        list0.add("TO TINGTEL \n at " + "4:23pm, Fri. 23rd March 2020");
        list0.add("From TINGTEL");
        list0.add("To " + receiver_number);
        stepView.setStepsViewIndicatorComplectingPosition(list0.size())
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

        stepView.setOnClickListener(v -> {
            Toast.makeText(this, "This is the current status of your transaction", Toast.LENGTH_SHORT).show();
        });
    }

    private void setStepViewToPendingStatus() {
        List<String> list0 = new ArrayList<>();
        list0.add(getResources().getString(R.string.naira) + sessionManager.getAmount() + " from " + phoneNumber + "(You)");
        list0.add("TO TINGTEL \n at " + "4:23pm, Fri. 23rd March 2020");
        list0.add("From TINGTEL");
        list0.add("To " + sessionManager.getReceiverPhoneNumber());
        stepView.setStepsViewIndicatorComplectingPosition(list0.size() - 2)
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

        stepView.setOnClickListener(v -> {
            Toast.makeText(this, "This is the current status of your transaction", Toast.LENGTH_SHORT).show();
        });
    }

    private void setStepViewToCompletedStatus() {
        List<String> list0 = new ArrayList<>();
        list0.add(getResources().getString(R.string.naira) + sessionManager.getAmount() + " from " + phoneNumber + "(You)");
        list0.add("TO TINGTEL \n at " + "4:23pm, Fri. 23rd March 2020");
        list0.add("From TINGTEL");
        list0.add("To " + sessionManager.getReceiverPhoneNumber());
        stepView.setStepsViewIndicatorComplectingPosition(list0.size())
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

        stepView.setOnClickListener(v -> Toast.makeText(this, "This is the current status of your transaction", Toast.LENGTH_SHORT).show());
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
                AppUtils.dismissLoadingDialog();

                if (sendOTPresponse.getTransactions() != null) {
                    Integer status = sendOTPresponse.getTransactions().getStatus();
                    if (status == 0) {
                        setStepViewToPendingStatus();
                    } else {
                        setStepViewToCompletedStatus();
                    }
                } else {
                    AppUtils.showDialog("No transactions found", StatusActivity.this);
                }
            }

            @Override
            public void onError(String error) {
                displayDialog(error, StatusActivity.this);

                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showSnackBar(String.valueOf(errorCode), complaintTextview);
                AppUtils.dismissLoadingDialog();
            }
        });
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
