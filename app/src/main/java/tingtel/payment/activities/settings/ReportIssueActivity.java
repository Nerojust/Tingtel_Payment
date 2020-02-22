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

        edTitle = findViewById(R.id.ed_title);
        edDetails = findViewById(R.id.ed_details);
        btnReport = findViewById(R.id.btn_report);

        btnReport.setOnClickListener(v -> AppUtils.showDialog("Thanks For Reporting your issues, we would look into it", this));

    }
}
