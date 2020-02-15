package tingtel.payment.fragments.settings;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import tingtel.payment.R;


public class TutorialFragment extends Fragment {


    String htmlTutorial;
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        webView = view.findViewById(R.id.webView);

        showTutorial();

        return view;
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
                    +  htmlText
                    + "</body></html>";

            webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);


    }

}
