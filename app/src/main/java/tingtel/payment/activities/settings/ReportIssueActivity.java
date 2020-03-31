package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.models.report_Issue.ReportIssueResponse;
import tingtel.payment.models.report_Issue.ReportIssueSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.ReportIssueInterface;

public class ReportIssueActivity extends AppCompatActivity {


    private EditText edTitle, edDetails;
    private Button btnReport;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        sessionManager = AppUtils.getSessionManagerInstance();
        edDetails = findViewById(R.id.ed_details);
        btnReport = findViewById(R.id.btn_report);

        btnReport.setOnClickListener(v -> {
            if (!edDetails.getText().toString().trim().isEmpty()) {
                if (edDetails.getText().toString().length() > 10) {
                    if (AppUtils.isNetworkAvailable(Objects.requireNonNull(this))) {
                        sendReport();
                    } else {
                        AppUtils.showSnackBar("No network available", edDetails);
                    }
                } else {
                    AppUtils.showSnackBar("Message is too short", edDetails);
                }
            } else {
                AppUtils.showSnackBar("Message box must not be empty", edDetails);
                edDetails.requestFocus();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
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
                AppUtils.showDialog("Thank you, we would look into it", ReportIssueActivity.this);
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
}
