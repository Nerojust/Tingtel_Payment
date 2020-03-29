package tingtel.payment.activities.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.qr_code.QRCodeDisplayActivity;
import tingtel.payment.activities.sign_in.SignInActivity;

public class SettingsActivity extends AppCompatActivity {

    Button logoutButton;
    private LinearLayout changePasswordLayout, changeEmailAddressLayout, manageSimLayout, tutorialLayout,
            reportIssueLayout, qrCodeLayout, shareAppLayout, deleteAccountLayout, privacyPolicyLayout, aboutUsLayout, backArrowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        initListeners();
    }
    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void initViews() {
        backArrowLayout = findViewById(R.id.backArrowLayout);
        qrCodeLayout = findViewById(R.id.qrCodeLayout);

        changePasswordLayout = findViewById(R.id.changePasswordLayout);
        changeEmailAddressLayout = findViewById(R.id.changeEmailLayout);
        manageSimLayout = findViewById(R.id.manageSimLayout);
        tutorialLayout = findViewById(R.id.tutorialLayout);
        reportIssueLayout = findViewById(R.id.reportIssueLayout);
        shareAppLayout = findViewById(R.id.shareAppLayout);
        deleteAccountLayout = findViewById(R.id.deleteAccountLayout);
        privacyPolicyLayout = findViewById(R.id.privacyPolicyLayout);
        aboutUsLayout = findViewById(R.id.aboutUsLayout);
        logoutButton = findViewById(R.id.logoutButton);
    }

    private void initListeners() {
        backArrowLayout.setOnClickListener(v -> finish());
        qrCodeLayout.setOnClickListener(v -> startActivity(new Intent(this, QRCodeDisplayActivity.class)));
        changePasswordLayout.setOnClickListener(v -> startActivity(new Intent(this, ChangePasswordActivity.class)));
        changeEmailAddressLayout.setOnClickListener(v -> startActivity(new Intent(this, ChangeEmailActivity.class)));
        manageSimLayout.setOnClickListener(v -> startActivity(new Intent(this, ManageSimActivity.class)));
        tutorialLayout.setOnClickListener(v -> startActivity(new Intent(this, TutorialActivity.class)));
        reportIssueLayout.setOnClickListener(v -> startActivity(new Intent(this, ReportIssueActivity.class)));

        shareAppLayout.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Select an app to share Tingtel with"));
            } catch (Exception e) {
                //e.toString();
            }
        });

        deleteAccountLayout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_account, viewGroup, false);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();

            Button btnYes = dialogView.findViewById(R.id.btn_yes);
            Button btnNo = dialogView.findViewById(R.id.btn_no);

            btnYes.setOnClickListener(v12 -> {
//todo: make network call
                Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            });

            btnNo.setOnClickListener(v1 -> alertDialog.dismiss());
            alertDialog.show();
        });

        privacyPolicyLayout.setOnClickListener(v -> {
            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tingtel.com/privacy-policy"));
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No Web browser Found.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        aboutUsLayout.setOnClickListener(v -> {

        });

        logoutButton.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.do_you_want_to_logout))
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        startActivity(new Intent(this, SignInActivity.class));
                        finishAffinity();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            android.app.AlertDialog alert = builder.create();
            alert.show();

        });
    }
}
