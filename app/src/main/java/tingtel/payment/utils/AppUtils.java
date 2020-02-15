package tingtel.payment.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import tingtel.payment.R;
import tingtel.payment.SignInActivity;

public class AppUtils {

    private static SessionManager sessionManager;

    public static SessionManager getSessionManagerInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }




    public static void dialUssdCode(Activity activity, String ussdCodeTodial, int simNumber) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //if android version doesnt require permission
            dialCode(ussdCodeTodial, activity, simNumber);
        } else {
            //if permission is not enabled, ask for permission
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                int REQUEST_PHONE_CALL = 101;
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            } else {
                dialCode(ussdCodeTodial, activity, simNumber);
            }
        }
    }

    /**
     * this method dials the number received for old versions.
     *
     * @param ussdCodeTodial
     * @param activity
     * @param simNumber
     */
    private static void dialCode(String ussdCodeTodial, Activity activity, int simNumber) {
        //check if it begins with * and ends with #
        if (ussdCodeTodial.startsWith("*") && ussdCodeTodial.endsWith("#")) {
            ussdCodeTodial = ussdCodeTodial.substring(0, ussdCodeTodial.length() - 1);
        }
        //create your own
        String finalUssdCode =  ussdCodeTodial + Uri.encode("#");
        //perform the action
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + finalUssdCode));
        intent.putExtra("com.android.phone.extra.slot", simNumber); //For sim 1
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }



    public static void showDialog(String message, Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_info, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tv_message);
        Button btnOk = (Button)dialogView.findViewById(R.id.btn_ok);

        tvMessage.setText(message);



        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

}
