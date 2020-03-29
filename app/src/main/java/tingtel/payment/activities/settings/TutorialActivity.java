package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.R;

public class TutorialActivity extends AppCompatActivity {

    String htmlTutorial;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        webView = findViewById(R.id.webView);

        showTutorial();

    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    private void showTutorial() {
        String mimeType = "text/html";
        String encoding = "utf-8";
        //TODO: Get this string from the server call
        String htmlText = "<h1 style=\"color: #FE4537; text-align: center;\">How To Use Tingtel</h1>\n" +
                "<p>Step 1: Open the app........</p>";

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/myfonts/Poppins-Regular_0.ttf\")}body{font-family: MyFont;color: #525252}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
    }
}
