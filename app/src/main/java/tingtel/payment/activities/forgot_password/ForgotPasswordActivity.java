package tingtel.payment.activities.forgot_password;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

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












//        emailAddressEdittext = findViewById(R.id.emailEdittext);
//        Button submitButton = findViewById(R.id.btn_submit);
//
//        submitButton.setOnClickListener(v -> {
//            retrievedEmailAaddress = emailAddressEdittext.getText().toString();
//
//            if (isValidFields()) {
//
//                if (AppUtils.isNetworkAvailable(Objects.requireNonNull(this))) {
//                    //send password to email
//                    Toast.makeText(this, "Password sent", Toast.LENGTH_SHORT).show();
//                } else {
//                    AppUtils.showSnackBar(getResources().getString(R.string.no_network_available), emailAddressEdittext);
//                }
//
//            }
//            //todo: make retrofit call to the forgot password endpoint and handle the response
//
//        });
    }

    /**
     * validate all the fields before proceeding
     *
     * @return true/false.
     */
    private boolean isValidFields() {
        if (emailAddressEdittext.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.email_is_required), emailAddressEdittext);
            emailAddressEdittext.requestFocus();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(retrievedEmailAaddress)) {
            AppUtils.showSnackBar(getResources().getString(R.string.invalid_characters), emailAddressEdittext);

            return false;
        }
        return true;
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