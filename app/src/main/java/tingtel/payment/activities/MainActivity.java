package tingtel.payment.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.R;
import tingtel.payment.activities.sign_in.SignInActivity;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;

public class MainActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!AppUtils.getSessionManagerInstance().isLogin()){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.do_you_want_to_logout))
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppUtils.getSessionManagerInstance().logout();
                    startActivity(intent);
                    MainActivity.this.onSuperBackPressed();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fade_out);
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void doLogout() {
        new Handler(Looper.getMainLooper()).post(() -> AppUtils.logOutInactivitySessionTimeout(this));
    }
}
