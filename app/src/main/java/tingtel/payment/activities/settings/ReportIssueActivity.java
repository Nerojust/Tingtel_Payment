package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.models.report_Issue.ReportIssueResponse;
import tingtel.payment.models.report_Issue.ReportIssueSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.ReportIssueInterface;

public class ReportIssueActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {


    private EditText edTitle, edDetails;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        sessionManager = AppUtils.getSessionManagerInstance();
        edDetails = findViewById(R.id.ed_details);
        Button btnReport = findViewById(R.id.btn_report);

        btnReport.setOnClickListener(v -> {
            if (!edDetails.getText().toString().trim().isEmpty()) {
                if (edDetails.getText().toString().length() > 10) {
                    if (AppUtils.isNetworkAvailable(Objects.requireNonNull(this))) {
                        sendReport();
                    } else {
                        AppUtils.showSnackBar(getResources().getString(R.string.no_network_available), edDetails);
                    }
                } else {
                    AppUtils.showSnackBar(getResources().getString(R.string.message_is_too_short), edDetails);
                }
            } else {
                AppUtils.showSnackBar(getResources().getString(R.string.message_box_must_not_be_empty), edDetails);
                edDetails.requestFocus();
            }
        });
    }

    private void sendReport() {
        AppUtils.initLoadingDialog(this);

        ReportIssueSendObject reportIssueSendObject = new ReportIssueSendObject();
        reportIssueSendObject.setEmail(sessionManager.getEmailFromLogin());
        reportIssueSendObject.setPhone(sessionManager.getNumberFromLogin());
        reportIssueSendObject.setMessage(edDetails.getText().toString().trim());
        reportIssueSendObject.setHash(AppUtils.generateHash(sessionManager.getEmailFromLogin(), sessionManager.getNumberFromLogin()));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(reportIssueSendObject);
        sessionManager.setReportIssueJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.reportAnIssue(reportIssueSendObject, new ReportIssueInterface() {
            @Override
            public void onSuccess(ReportIssueResponse reportIssueResponse) {
                AppUtils.dismissLoadingDialog();
                edDetails.setText("");
                edDetails.clearFocus();
                AppUtils.showDialog(getResources().getString(R.string.thank_you_we_will_look_into_it), ReportIssueActivity.this);
            }

            @Override
            public void onError(String error) {
                AppUtils.dismissLoadingDialog();
                AppUtils.showSnackBar(error, edDetails);
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.dismissLoadingDialog();
                AppUtils.showSnackBar(String.valueOf(errorCode), edDetails);
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
