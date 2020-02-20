package tingtel.payment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import tingtel.payment.utils.AppUtils;

public class QRCodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    final String[] permissions = new String[]{
            Manifest.permission.CAMERA
    };
    private Vibrator vibrate;
    private ZXingScannerView zXingScannerView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);
        zXingScannerView = new ZXingScannerView(getApplicationContext());

        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                scanBarcode();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 101);
            }
        } else {
            scanBarcode();
        }
    }

    private void scanBarcode() {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onStart() {
        super.onStart();
        zXingScannerView.startCamera();
        zXingScannerView.setResultHandler(this);
    }

    @Override
    public void handleResult(Result result) {
        //zXingScannerView.resumeCameraPreview(this);
        zXingScannerView.stopCamera();
        Intent intent = new Intent("barcodeSerialcaptured");
        AppUtils.getSessionManagerInstance().setScannedCodeResult(result.getText());
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        zXingScannerView.stopCamera();
        vibrate.vibrate(100);
        finish();
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            boolean isPerpermissionForAllGranted = false;
            if (grantResults.length > 0 && permissions.length == grantResults.length) {
                for (int i = 0; i < permissions.length; i++) {
                    isPerpermissionForAllGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            } else {
                isPerpermissionForAllGranted = true;
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            if (isPerpermissionForAllGranted) {
                scanBarcode();
            } else {
                //ActivityCompat.requestPermissions(MainActivity.this, permissions, 101);
                Toast.makeText(this, "Permission is needed", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
