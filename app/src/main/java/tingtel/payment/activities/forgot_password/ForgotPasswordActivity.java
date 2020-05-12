package tingtel.payment.activities.forgot_password;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import tingtel.payment.R;
import tingtel.payment.models.forgot_password.ForgotPasswordResponse;
import tingtel.payment.models.forgot_password.ForgotPasswordSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.ForgotPasswordInterface;

public class ForgotPasswordActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {
    private TextInputEditText emailAddressEdittext;
    private String retrievedEmailAaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

    }

    private void makeForgotPasswordRequest() {
        ForgotPasswordSendObject forgotPasswordSendObject = new ForgotPasswordSendObject();
        forgotPasswordSendObject.setEmail(retrievedEmailAaddress);
        //forgotPasswordSendObject.setPhone();
        //forgotPasswordSendObject.setPassword();
        //forgotPasswordSendObject.setHash();

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.forgotPassword(forgotPasswordSendObject, new ForgotPasswordInterface() {
            @Override
            public void onSuccess(ForgotPasswordResponse forgotPasswordResponse) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onErrorCode(int errorCode) {

            }
        });
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
