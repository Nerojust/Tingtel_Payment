package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import tingtel.payment.R;
import tingtel.payment.models.Report_Issue.ReportIssueResponse;
import tingtel.payment.models.Report_Issue.ReportIssueSendObject;
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
                    sendReport();
                } else {
                    AppUtils.showSnackBar("Message is too short", edDetails);
                }
            } else {
                AppUtils.showSnackBar("Message box must not be empty", edDetails);
                edDetails.requestFocus();
            }
        });
    }

    private void sendReport() {
        AppUtils.initLoadingDialog(this);

        ReportIssueSendObject reportIssueSendObject = new ReportIssueSendObject();
        reportIssueSendObject.setEmail(sessionManager.getEmailAddress());
        reportIssueSendObject.setPhone(sessionManager.getSimPhoneNumber());
        reportIssueSendObject.setMessage(edDetails.getText().toString().trim());
        reportIssueSendObject.setHash(AppUtils.generateHash(sessionManager.getEmailAddress(), sessionManager.getSimPhoneNumber()));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(reportIssueSendObject);
        sessionManager.setReportIssueJsonObject(jsonObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.reportAnIssue(reportIssueSendObject, new ReportIssueInterface() {
            @Override
            public void onSuccess(ReportIssueResponse reportIssueResponse) {
                AppUtils.dismissLoadingDialog();
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
                AppUtils.showSnackBar(String.valueOf(errorCode),edDetails);
            }
        });
    }
}
