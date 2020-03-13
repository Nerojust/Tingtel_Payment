package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;

public class ReportIssueActivity extends AppCompatActivity {


    private EditText edTitle, edDetails;
    private Button btnReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        edDetails = findViewById(R.id.ed_details);
        btnReport = findViewById(R.id.btn_report);

        btnReport.setOnClickListener(v -> {
            if (!edDetails.getText().toString().trim().isEmpty()) {
                AppUtils.showDialog("Thank you, we would look into it", this);

            } else {
                AppUtils.showSnackBar("Message box must not be empty", edDetails);
                edDetails.requestFocus();
            }
        });
    }
}
