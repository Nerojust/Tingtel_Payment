package tingtel.payment.activities.settings;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.adapters.SpinnerAdapter;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;
import tingtel.payment.models.add_sim.AddSimResponse;
import tingtel.payment.models.add_sim.AddSimSendObject;
import tingtel.payment.models.otp.SendOTPresponse;
import tingtel.payment.models.otp.SendOTPsendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.AddSimInterface;
import tingtel.payment.web_services.interfaces.SendOTPinterface;

public class AddSimActivity extends AppCompatActivity {
    private Spinner mSpinner;
    private String[] spinnerTitles;
    private String selectedSpinnerNetwork;
    private Button verifyButton;
    private TextInputEditText tvPhoneNumber;
    private SessionManager sessionManager;
    private TextView tvSimInfo;
    private String restructuredPhoneNumber;
    private String generatedOTP;
    private String retrievedPhoneNumber;
    private AlertDialog alertDialog;


    public void displayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup viewGroup = this.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.verify_otp, viewGroup, false);
        builder.setView(dialogView);
        alertDialog = builder.create();

        TextView resendTv = dialogView.findViewById(R.id.resendTV);
        PinView otpEdittext = dialogView.findViewById(R.id.otpPinview);
        Button verifyOtpButton = dialogView.findViewById(R.id.verifyButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        verifyOtpButton.setOnClickListener(v -> {
            String retrievedOTP = Objects.requireNonNull(otpEdittext.getText()).toString().trim();

            if (!Objects.requireNonNull(otpEdittext.getText()).toString().trim().isEmpty()) {
                if (retrievedOTP.length() == 4) {
                    if (generatedOTP.equalsIgnoreCase(retrievedOTP)) {

                        AppDatabase appdatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(this));
                        if (appdatabase.simCardsDao().getAllItems().size() > 4) {
                            if (appdatabase.simCardsDao().getNumber(retrievedPhoneNumber).size() > 0) {
                                AppUtils.showDialog("This Sim has already been registered, maximum of 4 sims allowed.", this);
                            } else {
                                saveSimDetails();
                                makeAddSimRequest();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                        otpEdittext.requestFocus();
                        otpEdittext.setText("");
                    }
                } else {
                    Toast.makeText(this, "must be 4 numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resendTv.setOnClickListener(v -> resendOTPtoCustomer());

        cancelButton.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }

    private void makeAddSimRequest() {
        AddSimSendObject addSimSendObject = new AddSimSendObject();
        addSimSendObject.setEmail(sessionManager.getEmailFromLogin());
        addSimSendObject.setPhone2(retrievedPhoneNumber);
        addSimSendObject.setUser_phone(sessionManager.getNumberFromLogin());
        addSimSendObject.setSim2_network(selectedSpinnerNetwork);
        addSimSendObject.setSim2_serial("");
        addSimSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.addSim(addSimSendObject, new AddSimInterface() {
            @Override
            public void onSuccess(AddSimResponse addSimResponse) {
                Toast.makeText(AddSimActivity.this, "Sim added successfully", Toast.LENGTH_LONG).show();
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(AddSimActivity.this, error, Toast.LENGTH_SHORT).show();
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                Toast.makeText(AddSimActivity.this, String.valueOf(errorCode), Toast.LENGTH_SHORT).show();
                AppUtils.dismissLoadingDialog();
            }
        });
    }

    private void resendOTPtoCustomer() {
        AppUtils.initLoadingDialog(this);

        SendOTPsendObject sendOTPsendObject = new SendOTPsendObject();
        sendOTPsendObject.setPhoneNumber(restructuredPhoneNumber);
        sendOTPsendObject.setNetwork(selectedSpinnerNetwork);
        sendOTPsendObject.setOtp(generatedOTP);
        sendOTPsendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.sendOTPtoCustomer(sendOTPsendObject, new SendOTPinterface() {
            @Override
            public void onSuccess(SendOTPresponse sendOTPresponse) {
                AppUtils.dismissLoadingDialog();
                AppUtils.showSnackBar("Code resent", tvPhoneNumber);

            }

            @Override
            public void onError(String error) {
                AppUtils.showDialog(error, AddSimActivity.this);

                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showSnackBar(String.valueOf(errorCode), tvPhoneNumber);
                AppUtils.dismissLoadingDialog();
            }
        });
    }


    private void saveSimDetails() {

        @SuppressLint("StaticFieldLeak")
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                Date queryDate = Calendar.getInstance().getTime();
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(AddSimActivity.this));

                //creating a task
                SimCards simCards = new SimCards();

                simCards.setSimNetwork(selectedSpinnerNetwork);
                simCards.setSimSerial(null);
                simCards.setPhoneNumber(retrievedPhoneNumber);

                //adding to database
                appdatabase.simCardsDao().insert(simCards);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(AddSimActivity.this, "Sim saved successfully", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
                finish();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sim);

        initViews();
        initListeners();
        initSpinner();
    }

    private void initSpinner() {
        spinnerTitles = new String[]{"Mtn", "Airtel", "9Mobile", "Glo"};
        int[] spinnerImages = new int[]{R.drawable.mtn_logo, R.drawable.airtel_logo, R.drawable.nmobile_logo, R.drawable.glo_logo};

        SpinnerAdapter mCustomAdapter = new SpinnerAdapter(Objects.requireNonNull(this), spinnerTitles, spinnerImages);
        mSpinner.setAdapter(mCustomAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSpinnerNetwork = spinnerTitles[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initListeners() {
        verifyButton.setOnClickListener(v -> {
            if (isValidFields()) {
                generatedOTP = AppUtils.generateOTP();
                retrievedPhoneNumber = Objects.requireNonNull(tvPhoneNumber.getText()).toString().trim();

                restructuredPhoneNumber = AppUtils.checkPhoneNumberAndRestructure(Objects.requireNonNull(retrievedPhoneNumber));

                sendOTPtoCustomer();
            }
        });
    }

    private void sendOTPtoCustomer() {
        AppUtils.initLoadingDialog(this);

        SendOTPsendObject sendOTPsendObject = new SendOTPsendObject();
        sendOTPsendObject.setPhoneNumber(restructuredPhoneNumber);
        sendOTPsendObject.setNetwork(selectedSpinnerNetwork);
        sendOTPsendObject.setOtp(generatedOTP);
        sendOTPsendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.sendOTPtoCustomer(sendOTPsendObject, new SendOTPinterface() {
            @Override
            public void onSuccess(SendOTPresponse sendOTPresponse) {
                AppUtils.dismissLoadingDialog();
                displayDialog();
            }

            @Override
            public void onError(String error) {
                AppUtils.showDialog(error, AddSimActivity.this);

                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showSnackBar(String.valueOf(errorCode), tvPhoneNumber);
                AppUtils.dismissLoadingDialog();
            }
        });
    }

    private boolean isValidFields() {
        if (Objects.requireNonNull(tvPhoneNumber.getText()).toString().isEmpty()) {
            AppUtils.showSnackBar("Number is required", tvPhoneNumber);
            tvPhoneNumber.requestFocus();
            return false;
        }
        if (tvPhoneNumber.getText().toString().length() < 11) {
            AppUtils.showSnackBar("Number is too short", tvPhoneNumber);
            tvPhoneNumber.requestFocus();
            return false;
        }

        return true;
    }

    private void initViews() {
        verifyButton = findViewById(R.id.btn_next);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvSimInfo = findViewById(R.id.tv_sim_info);
        mSpinner = findViewById(R.id.sp_network);
        sessionManager = AppUtils.getSessionManagerInstance();
    }
}
