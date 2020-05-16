package tingtel.payment.activities.credit_notify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.utils.AppUtils;

public class IncomingCreditDetailsActivity extends AppCompatActivity {
    private ImageView homeImageview, settingsImagview, backButtonImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_credit_details);

        initViews();
        initListeners();

        Intent intent = getIntent();
        if (intent != null) {
            String amount = intent.getStringExtra("amount");
            String sender_number = intent.getStringExtra("sender_number");
            String receiver_number = intent.getStringExtra("receiver_number");
            String date = intent.getStringExtra("date");

            TextView amountTextview = findViewById(R.id.amountTextview);
            TextView fromSender = findViewById(R.id.senderNumberTextview);
            TextView toReceiver = findViewById(R.id.receiveNumberTextview);
            TextView atDate = findViewById(R.id.dateTextview);

            amountTextview.setText(getResources().getString(R.string.naira) + amount);
            fromSender.setText(AppUtils.checkPhoneNumberAndRemovePrefix(sender_number));
            toReceiver.setText(AppUtils.checkPhoneNumberAndRemovePrefix(receiver_number));
            atDate.setText(date);
        }
    }

    private void initViews() {
        MobileAds.initialize(this, initializationStatus -> {});

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

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
