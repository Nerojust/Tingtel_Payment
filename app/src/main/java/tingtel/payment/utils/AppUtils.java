package tingtel.payment.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.sign_in.SignInActivity;

/**
 * Utilities class or helper class
 */
public class AppUtils {

    private static SessionManager sessionManager;
    private static ProgressDialog progress;

    public static SessionManager getSessionManagerInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String generateHash(String input1, String input2) {
        return getSHA512(input1 + input2 + BuildConfig.HASH_KEY);
    }

    public static String generateRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }

    public static String generateOTP() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    public static void showProgressTracker(View view, Context context) {

        String[] descriptionData = {context.getResources().getString(R.string.sender), context.getResources().getString(R.string.receiver), context.getResources().getString(R.string.summary)};
        StateProgressBar stateProgressBar = view.findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateDescriptionTypeface("font/rubik_regular.ttf");
        stateProgressBar.setStateNumberTypeface("font/rubik_regular.ttf");
    }

    public static String checkPhoneNumberAndRestructure(String number) {
        if (number.substring(0, 1).equals("0")) {
            number = number.substring(1);
            number = "234" + number;
            return number;
        } else if (number.substring(0, 2).equals("+0")) {
            number = number.replace("+0", "234");
            return number;
        } else if (number.substring(0, 4).equals("+234")) {
            number = number.replace("+234", "234");
            return number;
        } else {
            return number;
        }
    }

    public static void changeStatusOfButton(Context context, Button button, boolean isEnabled) {
        if (isEnabled) {
            button.setBackground(context.getResources().getDrawable(R.drawable.dashboard_buttons));
            button.setEnabled(true);
            button.setClickable(true);
        } else {
            button.setBackground(context.getResources().getDrawable(R.drawable.dashboard_buttons_gray));
            button.setEnabled(false);
            button.setClickable(false);
        }
    }

    public static String checkPhoneNumberAndRemovePrefix(String number) {
        if (number.substring(0, 3).equals("234")) {
            if (number.startsWith("234")) {
                return "0" + number.substring("234".length());
            }
            return number;
        } else if (number.substring(0, 4).equals("+234")) {
            number = number.replace("+234", "0");
            return number;
        } else {
            return number;
        }
    }

    public static void showSnackBar(String msg, View view) {
        Snackbar mySnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        Vibrator vibrator = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(Constants.VIBRATOR_INTEGER);
        }
        mySnackbar.show();
    }

    public static boolean isValidFieldsNumbersAndLetters(String inputtext) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9.,\\s]{3,40}$");
        Matcher matcher = pattern.matcher(inputtext);
        return matcher.matches();
    }

    public static boolean isValidEmailAddress(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    /**
     * version check before dialing codes
     *
     * @param activity
     * @param ussdCodeTodial
     * @param simNumber
     */
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
        String finalUssdCode = ussdCodeTodial + Uri.encode("#");
        //perform the action
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + finalUssdCode));
        intent.putExtra("com.android.phone.extra.slot", simNumber); //For sim 1
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * to display a dialog
     *
     * @param message:  message to be displayed
     * @param activity: Get the calling activity
     */
    public static void showDialog(String message, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_info, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        Button btnOk = dialogView.findViewById(R.id.btn_ok);

        tvMessage.setText(message);
        btnOk.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }

    public static boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }

    public static void initLoadingDialog(Context context) {
        progress = ProgressDialog.show(context, null, null, true);
        progress.setContentView(R.layout.progress_dialog_element);
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(progress.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progress.show();
    }

    public static void dismissLoadingDialog() {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }


    @SuppressLint("NewApi")
    public static String getSHA512(String input) {
        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes(StandardCharsets.UTF_8));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }


    public static void logOutInactivitySessionTimeout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.alert));
        builder.setMessage(activity.getResources().getString(R.string.session_timeout));
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getResources().getString(R.string.okay), (dialog, id) -> {
            Intent intent = new Intent(activity, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppUtils.getSessionManagerInstance().logout();
            activity.finish();
            activity.startActivity(intent);

        });

        try {
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
