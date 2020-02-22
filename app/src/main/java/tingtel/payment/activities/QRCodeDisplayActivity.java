package tingtel.payment.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
    private Bitmap bitmapSim1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodedisplay);
        sessionManager = AppUtils.getSessionManagerInstance();

        initViews();
        initListeners();
    }

    private void initViews() {
        qrSim1Imageview = findViewById(R.id.simOneQRCodeImageview);
        qrSim2Imageview = findViewById(R.id.simTwoQRCodeImageview);
        getCodeButton = findViewById(R.id.getCodeButton);
        radioGroup = findViewById(R.id.radioGroup);
        shareQR1 = findViewById(R.id.shareSim1);
        shareQR2 = findViewById(R.id.shareSim2);

        radioGroupLayout = findViewById(R.id.radioGroupLayout);
        qr1Layout = findViewById(R.id.qr1Layout);
        qr2Layout = findViewById(R.id.qr2Layout);

        if (sessionManager.getTotalNumberOfSimsDetectedOnDevice() == 1) {
            radioGroupLayout.setVisibility(View.GONE);

            if (sessionManager.getSimPhoneNumber() != null) {
                try {
                    convertToQRcode(AppUtils.getSessionManagerInstance().getSimPhoneNumber(), qrSim1Imageview);
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
            }
        } else if (sessionManager.getTotalNumberOfSimsDetectedOnDevice() == 2) {
            radioGroupLayout.setVisibility(View.VISIBLE);

            if (sessionManager.getSimPhoneNumber() != null) {
                if (sessionManager.getSimPhoneNumber1() != null) {
                    try {
                        convertToQRcode(AppUtils.getSessionManagerInstance().getSimPhoneNumber1(), qrSim2Imageview);
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
            }
        }
    }

    private void initListeners() {
        getCodeButton.setOnClickListener(v -> {
            if (radioGroup.getCheckedRadioButtonId() != -1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                switch (selectedId) {
                    case R.id.radioSim1:
                        if (sessionManager.getSimPhoneNumber() != null) {
                            try {
                                convertToQRcode(AppUtils.getSessionManagerInstance().getSimPhoneNumber(), qrSim1Imageview);
                                qr2Layout.setVisibility(View.GONE);
                                qr1Layout.setVisibility(View.VISIBLE);
                                Bitmap bm = ((BitmapDrawable) qrSim1Imageview.getDrawable()).getBitmap();
                                shareQR1.setOnClickListener(v1 -> {
                                    String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bm, "title", "lets go there");
                                    Uri bitmapUri = Uri.parse(bitmapPath);

                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("image/png");
                                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                                    startActivity(Intent.createChooser(intent, "Share"));

                                });
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(this, "Please register a sim first", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.radioSim2:
                        if (sessionManager.getSimPhoneNumber1() != null) {
                            try {
                                convertToQRcode(AppUtils.getSessionManagerInstance().getSimPhoneNumber1(), qrSim2Imageview);
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


    }


    private void convertToQRcode(String phoneNumber, ImageView imageView) throws WriterException {

        BitMatrix bitMatrix = multiFormatWriter.encode(phoneNumber, BarcodeFormat.QR_CODE, 600, 600);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        bitmapSim1 = barcodeEncoder.createBitmap(bitMatrix);
        imageView.setImageBitmap(bitmapSim1);
    }
}
