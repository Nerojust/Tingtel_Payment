package tingtel.payment.activities.history;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.VerticalStepView;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.utils.AppUtils;

public class StatusActivity extends AppCompatActivity {
    private VerticalStepView stepView;
    private ImageView backButtonImageview, homeImageview, settingsImageview;

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
    }

    private void initViews() {
        backButtonImageview = findViewById(R.id.backArrowLayout);
        homeImageview = findViewById(R.id.homeImageview);
        settingsImageview = findViewById(R.id.settingsImageview);

        stepView = findViewById(R.id.step_view);

        List<String> list0 = new ArrayList<>();
        list0.add(getResources().getString(R.string.naira) + "100 from " + AppUtils.getSessionManagerInstance().getSimPhoneNumber());
        list0.add("TO TINGTEL PAY");
        list0.add("From TINGTEL PAY");
        list0.add("To 08034567898");
        stepView.setStepsViewIndicatorComplectingPosition(list0.size() - 2)
                .reverseDraw(false)//default is true
                .setStepViewTexts(list0)
                .setLinePaddingProportion(1f)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, R.color.tingtel_red_color))
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.black))
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, R.color.tingtel_red_color))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.tingtel_red_color))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.ic_check_circle))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.attention));


    }
}
