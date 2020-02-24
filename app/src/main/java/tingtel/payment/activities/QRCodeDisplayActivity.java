package tingtel.payment.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
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

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

public class QRCodeDisplayActivity extends AppCompatActivity {
    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    LinearLayout radioGroupLayout, qr1Layout, qr2Layout;
    RadioGroup radioGroup;
    private RadioButton rbSim1,rbSim2;
    ImageView qrSim1Imageview, qrSim2Imageview, shareQR1, shareQR2;
    Button getCodeButton;
    SessionManager sessionManager;
    private Bitmap bitmapSim1;

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
        rbSim1 =findViewById(R.id.radioSim1);
        rbSim2 =findViewById(R.id.radioSim2);
        shareQR1 = findViewById(R.id.shareSim1);
        shareQR2 = findViewById(R.id.shareSim2);

        radioGroupLayout = findViewById(R.id.radioGroupLayout);
        qr1Layout = findViewById(R.id.qr1Layout);
        qr2Layout = findViewById(R.id.qr2Layout);

        radioGroupLayout.setVisibility(View.GONE);
        qr2Layout.setVisibility(View.GONE);
        qr1Layout.setVisibility(View.GONE);

        if (sessionManager.getTotalNumberOfSimsDetectedOnDevice() == 1) {
            if (sessionManager.getSimPhoneNumber() != null) {
                try {
                    convertToQRcode(AppUtils.getSessionManagerInstance().getSimPhoneNumber(), qrSim1Imageview);
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
                Toast.makeText(this, "Sim 1 phone number is empty. Add sim to profile", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (sessionManager.getTotalNumberOfSimsDetectedOnDevice() == 2) {
            radioGroupLayout.setVisibility(View.VISIBLE);
            populateSimRadioButtons();
            /*if (sessionManager.getSimPhoneNumber() != null) {
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
                        } else {
                            Toast.makeText(this, "Please register a sim first", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.radioSim2:
                        if (!sessionManager.getNetworkName1().equalsIgnoreCase("ntel")) {
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
                        }else
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


    }
    private void populateSimRadioButtons() {
        String NoOfSIm = sessionManager.getSimStatus();

        switch (NoOfSIm) {
            case "SIM1":
                rbSim1.setVisibility(View.VISIBLE);
                rbSim1.setVisibility(View.GONE);
                rbSim1.setText(sessionManager.getNetworkName());
                break;

            case "SIM1 SIM2":
                rbSim1.setVisibility(View.VISIBLE);
                rbSim2.setVisibility(View.VISIBLE);
                rbSim1.setText(sessionManager.getNetworkName() + " (" + sessionManager.getSimPhoneNumber() + ")");
                rbSim2.setText(sessionManager.getNetworkName1() + " (" + sessionManager.getSimPhoneNumber1() + ")");
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
