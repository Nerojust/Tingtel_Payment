package tingtel.payment.fragments.transfer_airtime;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import tingtel.payment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetTransferPinTutorialFragment extends Fragment {

    private String Network;
    private WebView webView;
    private TextView tvNetworkName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_transfer_pin_tutorial, container, false);
        Network = Objects.requireNonNull(getArguments()).getString("network");
        webView = view.findViewById(R.id.webView);
        tvNetworkName = view.findViewById(R.id.tv_network_name);

        showTutorial();

        return view;
    }


    private void showTutorial() {

        tvNetworkName.setText("Set Your Pin (" + Network + ")");

        String mimeType = "text/html";
        String encoding = "utf-8";
        String htmlText;
        //TODO: Get this string from the server call

        if (Network.equalsIgnoreCase("Mtn")) {

            htmlText = "<h2 id=\"3cfe\" class=\"ie if cc at as bu ig ih ii ij ik il im in io ip iq\" data-selectable-paragraph=\"\"><strong class=\"bf\">MTN, Share &rsquo;N&rsquo; Sell</strong></h2>\n" +
                    "<p id=\"3f7b\" class=\"gt gu cc at gv b gw ir gy is ha it hc iu he iv hg dt\" data-selectable-paragraph=\"\">MTN Share &rsquo;N&rsquo; Sell comes with a pin feature. Before you can transfer credit, you have to change the default pin which is 0000.</p>\n" +
                    "<ul class=\"\">\n" +
                    "<li id=\"a99f\" class=\"gt gu cc at gv b gw gx gy gz ha hb hc hd he hf hg iw ix iy\" data-selectable-paragraph=\"\">To change your default PIN,</li>\n" +
                    "<li id=\"f123\" class=\"gt gu cc at gv b gw iz gy ja ha jb hc jc he jd hg iw ix iy\" data-selectable-paragraph=\"\">Send a text message with&nbsp;<strong class=\"gv je\">default PIN</strong>&nbsp;+<strong class=\"gv je\">New PIN</strong>&nbsp;+<strong class=\"gv je\">New PIN</strong>&nbsp;to&nbsp;<strong class=\"gv je\">777</strong>.</li>\n" +
                    "</ul>\n" +
                    "<p id=\"d3bd\" class=\"gt gu cc at gv b gw gx gy gz ha hb hc hd he hf hg dt\" data-selectable-paragraph=\"\"><strong class=\"gv je\"><em class=\"jf\">For example, to change the default PIN, SMS &lsquo;0000 1234 1234&rsquo; to 777</em></strong>.</p>\n" +
                    "<ul class=\"\">\n" +
                    "<li id=\"c2d6\" class=\"gt gu cc at gv b gw gx gy gz ha hb hc hd he hf hg iw ix iy\" data-selectable-paragraph=\"\">MTN Share &rsquo;N&rsquo; Sell allows you to transfer a minimum amount of NGN 50.00 to a maximum of NGN5, 000.00 on each transaction.</li>\n" +
                    "<li id=\"b1f2\" class=\"gt gu cc at gv b gw iz gy ja ha jb hc jc he jd hg iw ix iy\" data-selectable-paragraph=\"\">You can also transfer up to a total amount of NGN10,000.00 every day!</li>\n" +
                    "</ul>";

        } else if (Network.equalsIgnoreCase("Airtel")) {

            htmlText = "<h2 id=\"2b07\" class=\"ie if cc at as bu ig ih ii ij ik il im in io ip iq\" data-selectable-paragraph=\"\">Airtel Me2U</h2>\n" +
                    "<p id=\"b480\" class=\"gt gu cc at gv b gw ir gy is ha it hc iu he iv hg dt\" data-selectable-paragraph=\"\"><mark class=\"or os kl\">To transfer airtime on Airtel Nigeria&rsquo;s &ldquo;Me2U&rdquo;, create a 4-DIGIT-PIN.</mark></p>\n" +
                    "<ul class=\"\">\n" +
                    "<li id=\"cd1b\" class=\"gt gu cc at gv b gw gx gy gz ha hb hc hd he hf hg iw ix iy\" data-selectable-paragraph=\"\">To change/create your airtime transfer PIN on Airtel</li>\n" +
                    "<li id=\"b2df\" class=\"gt gu cc at gv b gw iz gy ja ha jb hc jc he jd hg iw ix iy\" data-selectable-paragraph=\"\">Send a text in the following format to 432:&nbsp;<strong class=\"gv je\">PIN 5555</strong>&nbsp;to&nbsp;<strong class=\"gv je\">432&nbsp;<em class=\"jf\">E.g. to change password to 4321, the text sent will read &mdash; PIN 1234 432 .</em></strong></li>\n" +
                    "<li id=\"252e\" class=\"gt gu cc at gv b gw iz gy ja ha jb hc jc he jd hg iw ix iy\" data-selectable-paragraph=\"\">You will then receive an SMS confirmation from 432.</li>\n" +
                    "</ul>";

        } else if (Network.equalsIgnoreCase("Glo")) {

            htmlText = "<h2 id=\"b8da\" class=\"ie if cc at as bu ig ih ii ij ik il im in io ip iq\" data-selectable-paragraph=\"\">Glo EasyShare</h2>\n" +
                    "<p id=\"9027\" class=\"gt gu cc at gv b gw ir gy is ha it hc iu he iv hg dt\" data-selectable-paragraph=\"\">To transfer airtime on Glo network; Activate the service by creating your Glo EasyShare PIN.</p>\n" +
                    "<ul class=\"\">\n" +
                    "<li id=\"80b9\" class=\"gt gu cc at gv b gw gx gy gz ha hb hc hd he hf hg iw ix iy\" data-selectable-paragraph=\"\">To change/create your Glo EasyShare 5-digit PIN:</li>\n" +
                    "<li id=\"9f42\" class=\"gt gu cc at gv b gw iz gy ja ha jb hc jc he jd hg iw ix iy\" data-selectable-paragraph=\"\">Dial *132*00000*New Pin*New Pin#&nbsp;<strong class=\"gv je\"><em class=\"jf\">For example if you want your new pin to be 12345 dial *132*00000*12345*12345#</em></strong></li>\n" +
                    "<li id=\"cbf9\" class=\"gt gu cc at gv b gw iz gy ja ha jb hc jc he jd hg iw ix iy\" data-selectable-paragraph=\"\">To Transfer Credit, dial *131*Phone Number of the recipient*Amount*Password#</li>\n" +
                    "<li id=\"d138\" class=\"gt gu cc at gv b gw iz gy ja ha jb hc jc he jd hg iw ix iy\" data-selectable-paragraph=\"\">For example to Transfer N50 to 08055555555 and your pin is 12345 dial *131*08055555555*50*12345#.</li>\n" +
                    "</ul>\n" +
                    "<p id=\"6a43\" class=\"gt gu cc at gv b gw gx gy gz ha hb hc hd he hf hg dt\" data-selectable-paragraph=\"\">You will receive a text message once the transfer is completed. Default password is 00000.</p>";

        } else if (Network.equalsIgnoreCase("9mobile")) {
            htmlText = "<h2 id=\"238b\" class=\"ie if cc at as bu ig ih ii ij ik il im in io ip iq\" data-selectable-paragraph=\"\">9mobile Airtime Transfer</h2>\n" +
                    "<ul class=\"\">\n" +
                    "<li id=\"d2f6\" class=\"gt gu cc at gv b gw ir gy is ha it hc iu he iv hg iw ix iy\" data-selectable-paragraph=\"\">You activate the service by setting up your transfer pin.</li>\n" +
                    "<li id=\"c0c6\" class=\"gt gu cc at gv b gw iz gy ja ha jb hc jc he jd hg iw ix iy\" data-selectable-paragraph=\"\">The default 4-digit PIN is 0000.</li>\n" +
                    "<li id=\"2910\" class=\"gt gu cc at gv b gw iz gy ja ha jb hc jc he jd hg iw ix iy\" data-selectable-paragraph=\"\">To change your airtime transfer PIN on Etisalat Dial: *247*0000* NEW PIN#<strong class=\"gv je\">&nbsp;<em class=\"jf\">For example, to change your PIN to 1234; Dial *247*0000*1234#</em></strong></li>\n" +
                    "</ul>";
        } else {
            htmlText = "";
        }


        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/myfonts/Poppins-Regular_0.ttf\")}body{font-family: MyFont;color: #525252}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
    }
}
