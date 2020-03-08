package tingtel.payment.activities.history;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.VerticalStepView;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;

public class StatusActivity extends AppCompatActivity {
    VerticalStepView stepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        initViews();

    }

    private void initViews() {
        stepView = findViewById(R.id.step_view);

        List<String> list0 = new ArrayList<>();
        list0.add(getResources().getString(R.string.naira) + "100 from " + AppUtils.getSessionManagerInstance().getSimPhoneNumber());
        list0.add("TO TINGTEL");
        list0.add("From Tingtel");
        list0.add("To 08034567898");
        stepView.setStepsViewIndicatorComplectingPosition(list0.size() - 2)
                .reverseDraw(false)//default is true
                .setStepViewTexts(list0)
                .setLinePaddingProportion(1f)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, R.color.tingtel_red_color))
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, R.color.tingtel_red_color))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.ic_check_circle))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.attention));

    }
}
