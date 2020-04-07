package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;

public class TutorialActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {

    String htmlTutorial;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        webView = findViewById(R.id.webView);

        showTutorial();

    }

    private void showTutorial() {
        String mimeType = "text/html";
        String encoding = "utf-8";
        //TODO: Get this string from the server call
        String htmlText = "<h3 style=\"color: #FE4537; text-align: center;\">How To Use Tingtel</h3>\n" +
                "<p>Step 1: Open the app........</p>";

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/myfonts/Poppins-Regular_0.ttf\")}body{font-family: MyFont;color: #525252}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
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
