package tingtel.payment.activities.credit_notify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.SettingsActivity;

public class IncomingCreditNotifidationActivity extends AppCompatActivity {
    private ImageView homeImageview, settingsImagview, backButtonImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_credit_notifidation);

        initViews();
        initListeners();
    }

    private void initViews() {
        backButtonImageview = findViewById(R.id.backArrowLayout);
        homeImageview = findViewById(R.id.homeImageview);
        settingsImagview = findViewById(R.id.settingsImageview);
    }

    private void initListeners() {
        backButtonImageview.setOnClickListener(v -> {
            finish();
        });

        homeImageview.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        });

        settingsImagview.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

}
