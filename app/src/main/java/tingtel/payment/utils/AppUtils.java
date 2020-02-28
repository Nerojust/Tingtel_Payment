package tingtel.payment.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import tingtel.payment.R;

/**
 * Utilities class or helper class
 */
public class AppUtils {

    private static SessionManager sessionManager;

    public static SessionManager getSessionManagerInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public static void showSnackBar(String msg, View view) {
        Snackbar mySnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        Vibrator vibrator = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(Constants.VIBRATOR_INTEGER);
        mySnackbar.show();
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

    public static void deleteFile(Context context, String filepath) {
        File file = new File(filepath);
        file.delete();
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    public static void deleteFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }
    }

    public static boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }

    //left
    public static void saveCustomerLeftThumbToGallery(Context context, String filename, Bitmap bitmap) {
        String barcodeImage = null;
        try {
            barcodeImage = Environment.getExternalStorageDirectory() + "//VPOS_DETAILS//FINGERPRINT_DATA";
            File file = new File(barcodeImage);
            if (!file.exists()) {
                file.mkdirs();
            }
            barcodeImage = barcodeImage + "//" + filename;

            AppUtils.getSessionManagerInstance().setImageStoragePath(barcodeImage);
            file = new File(barcodeImage);
            if (!file.exists()) {
                file.createNewFile();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(barcodeImage));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, dos);
            dos.flush();
            dos.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = null;
        if (barcodeImage != null) {
            file = new File(barcodeImage);
        }
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
