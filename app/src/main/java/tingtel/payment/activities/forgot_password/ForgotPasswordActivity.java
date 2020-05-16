package tingtel.payment.activities.forgot_password;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;

public class ForgotPasswordActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {
    private TextInputEditText emailAddressEdittext;
    private String retrievedEmailAaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

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
