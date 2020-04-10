package tingtel.payment.activities.qr_code;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

public class QRCodeDisplayActivity extends AppCompatActivity {
    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    LinearLayout radioGroupLayout, qr1Layout, qr2Layout;
    RadioGroup radioGroup;
    ImageView qrSim1Imageview, qrSim2Imageview, shareQR1, shareQR2;
    Button getCodeButton;
    SessionManager sessionManager;
    private RadioButton rbSim1, rbSim2;
    private Bitmap bitmapSim1;
    private String path;
    private String tingtelPath;
    private File directoryForTingtel;
    private String timeStamp;
    private String fileName;
    private String mCurrentQRImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodedisplay);
        getSupportActionBar().setTitle("Get QR Code");
        sessionManager = AppUtils.getSessionManagerInstance();

        initViews();
        initListeners();
    }

    private void initViews() {
        qrSim1Imageview = findViewById(R.id.simOneQRCodeImageview);
        qrSim2Imageview = findViewById(R.id.simTwoQRCodeImageview);
        getCodeButton = findViewById(R.id.getCodeButton);
        radioGroup = findViewById(R.id.radioGroup);
        rbSim1 = findViewById(R.id.radioSim1);
        rbSim2 = findViewById(R.id.radioSim2);
        shareQR1 = findViewById(R.id.shareSim1);
        shareQR2 = findViewById(R.id.shareSim2);

        radioGroupLayout = findViewById(R.id.radioGroupLayout);
        qr1Layout = findViewById(R.id.qr1Layout);
        qr2Layout = findViewById(R.id.qr2Layout);

        radioGroupLayout.setVisibility(View.GONE);
        qr2Layout.setVisibility(View.GONE);
        qr1Layout.setVisibility(View.GONE);

        if (sessionManager.getTotalNumberOfSimsDetectedOnDevice() == 1) {
            if (sessionManager.getSimOnePhoneNumber() != null) {
                try {
                    convertToQRcode(AppUtils.getSessionManagerInstance().getSimOnePhoneNumber(), qrSim1Imageview);
                    qr1Layout.setVisibility(View.VISIBLE);


                } catch (WriterException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Sim 1 phone number is empty. Add sim to profile", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (sessionManager.getTotalNumberOfSimsDetectedOnDevice() == 2) {
            radioGroupLayout.setVisibility(View.VISIBLE);
            populateSimRadioButtons();
            /*if (sessionManager.getSimOnePhoneNumber() != null) {
                if (sessionManager.getSimTwoPhoneNumber() != null) {
                    try {
                        convertToQRcode(AppUtils.getSessionManagerInstance().getSimTwoPhoneNumber(), qrSim2Imageview);
                        qr2Layout.setVisibility(View.VISIBLE);
                        qr1Layout.setVisibility(View.GONE);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    shareQR2.setOnClickListener(v1 -> {

                    });
                } else {
                    Toast.makeText(this, "Please register sim 2", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please register sim 1 first", Toast.LENGTH_SHORT).show();
            }*/
        } else if (sessionManager.getTotalNumberOfSimsDetectedOnDevice() == 0) {
            radioGroupLayout.setVisibility(View.GONE);
            qr1Layout.setVisibility(View.GONE);
            qr2Layout.setVisibility(View.GONE);
            Toast.makeText(this, "No sim card detected. Insert a sim", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initListeners() {
        getCodeButton.setOnClickListener(v -> {
            if (radioGroup.getCheckedRadioButtonId() != -1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                switch (selectedId) {
                    case R.id.radioSim1:
                        if (sessionManager.getSimOnePhoneNumber() != null) {
                            try {
                                convertToQRcode(AppUtils.getSessionManagerInstance().getSimOnePhoneNumber(), qrSim1Imageview);
                                qr2Layout.setVisibility(View.GONE);
                                qr1Layout.setVisibility(View.VISIBLE);
                                /*Bitmap bm = ((BitmapDrawable) qrSim1Imageview.getDrawable()).getBitmap();
                                shareQR1.setOnClickListener(v1 -> {
                                    String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bm, "title", "lets go there");
                                    Uri bitmapUri = Uri.parse(bitmapPath);

                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("image/png");
                                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                                    startActivity(Intent.createChooser(intent, "Share"));

                                });*/
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(this, "Please register a sim first", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.radioSim2:
                        if (!sessionManager.getSimTwoNetworkName().equalsIgnoreCase("ntel")) {
                            if (sessionManager.getSimTwoPhoneNumber() != null) {
                                try {
                                    convertToQRcode(AppUtils.getSessionManagerInstance().getSimTwoPhoneNumber(), qrSim2Imageview);
                                    qr2Layout.setVisibility(View.VISIBLE);
                                    qr1Layout.setVisibility(View.GONE);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }

                                shareQR2.setOnClickListener(v1 -> {

                                });
                            } else {
                                Toast.makeText(this, "Please register a sim first", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(this, "Network not supported", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        qr2Layout.setVisibility(View.GONE);
                        qr1Layout.setVisibility(View.GONE);
                        break;
                }

            } else {
                Toast.makeText(this, "Please select a sim to get qr code", Toast.LENGTH_SHORT).show();
            }
        });

        shareQR1.setOnClickListener(v -> {
            if (AppUtils.hasImage(qrSim1Imageview)) {

                Drawable mDrawable = qrSim1Imageview.getDrawable();
                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "TingtelQRCode", null);

                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_TEXT, "Hey please scan to pay at " + "https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(intent, "Share Tingtel QR Code with"));


            } else Toast.makeText(this, "Get a qr code first", Toast.LENGTH_SHORT).show();
        });
        shareQR2.setOnClickListener(v -> {
            if (AppUtils.hasImage(qrSim2Imageview)) {

                Drawable mDrawable = qrSim2Imageview.getDrawable();
                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "TingtelQRCode", null);

                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_TEXT, "Hey please scan to pay at " + "https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(intent, "Share Tingtel QR Code with"));


            } else Toast.makeText(this, "Get a qr code first", Toast.LENGTH_SHORT).show();

        });
    }


    private void populateSimRadioButtons() {
        String NoOfSIm = sessionManager.getSimStatus();

        switch (NoOfSIm) {
            case "SIM1":
                rbSim1.setVisibility(View.VISIBLE);
                rbSim1.setVisibility(View.GONE);
                rbSim1.setText(sessionManager.getSimOneNetworkName());
                break;

            case "SIM1 SIM2":
                rbSim1.setVisibility(View.VISIBLE);
                rbSim2.setVisibility(View.VISIBLE);
                rbSim1.setText(sessionManager.getSimOneNetworkName() + " (" + sessionManager.getSimOnePhoneNumber() + ")");
                rbSim2.setText(sessionManager.getSimTwoNetworkName() + " (" + sessionManager.getSimTwoPhoneNumber() + ")");
                break;
        }
    }

    private void convertToQRcode(String phoneNumber, ImageView imageView) throws WriterException {

        BitMatrix bitMatrix = multiFormatWriter.encode(phoneNumber, BarcodeFormat.QR_CODE, 600, 600);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        bitmapSim1 = barcodeEncoder.createBitmap(bitMatrix);

        imageView.setImageBitmap(bitmapSim1);
    }
}
