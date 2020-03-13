package tingtel.payment.activities.history;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.VerticalStepView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.ReportIssueActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

public class StatusActivity extends AppCompatActivity {
    private VerticalStepView stepView;
    private ImageView backButtonImageview, homeImageview, settingsImageview;
    private TextView complaintTextview, referenceIdTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        initViews();
        initListeners();
    }

    private void initListeners() {
        backButtonImageview.setOnClickListener(v -> finish());

        homeImageview.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finishAffinity();

        });

        settingsImageview.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        complaintTextview.setOnClickListener(v -> startActivity(new Intent(this, ReportIssueActivity.class)));
    }

    private void initViews() {
        backButtonImageview = findViewById(R.id.backArrowLayout);
        homeImageview = findViewById(R.id.homeImageview);
        settingsImageview = findViewById(R.id.settingsImageview);
        complaintTextview = findViewById(R.id.complaintTextview);
        referenceIdTextview = findViewById(R.id.referenceIdTextview);
        //generate random string for transaction.

        String randomString = AppUtils.generateRandomString();
        referenceIdTextview.setText(randomString);

        SessionManager sessionManager = AppUtils.getSessionManagerInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, hh:mm a");
        stepView = findViewById(R.id.step_view);

        List<String> list0 = new ArrayList<>();
        list0.add(getResources().getString(R.string.naira) + sessionManager.getAmount() + " from " + sessionManager.getSimPhoneNumber() + "(You)");
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
}
