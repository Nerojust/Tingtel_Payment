package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.utils.SessionManager;

public class ManageSimActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {
    private TextView tvPhoneNumber1, tvPhoneNumber2, tvPhoneNumber3, tvPhoneNumber4, tvNetworkName1, tvNetworkName2, tvNetworkName3, tvNetworkName4;
    private ImageView imgDelete1, imgDelete2, imgDelete3, imgDelete4, imageNetwork1, imageNetwork2, imageNetwork3, imageNetwork4;
    private Switch on_off_switch1, on_off_switch2, on_off_switch3, on_off_switch4;

    private SessionManager sessionManager;
    private String retrievedSim1Number;
    private String retrievedSim2Number;
    private String retrievedSim3Number;
    private String retrievedSim4Number;
    private String retrievedSim1Network;
    private String retrievedSim2Network;
    private String retrievedSim3Network;
    private String retrievedSim4Network;
    private String retrievedSim1Serial;
    private String retrievedSim2Serial;
    private String retrievedSim3Serial;
    private String retrievedSim4Serial;
    private String serial1onDevice;
    private String serial2onDevice;
    private LinearLayout sim1Layout, sim2Layout, sim3Layout, sim4Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sim);
        initSettings();

        initViews();
        initListeners();
    }

    private void initSettings() {
        sessionManager = AppUtils.getSessionManagerInstance();

        serial1onDevice = sessionManager.getSimSerialICCID();
        serial2onDevice = sessionManager.getSimSerialICCID1();

        retrievedSim1Number = sessionManager.getSimOnePhoneNumberFromLogin();
        retrievedSim2Number = sessionManager.getSimTwoPhoneNumberFromLogin();
        retrievedSim3Number = sessionManager.getSimThreePhoneNumberFromLogin();
        retrievedSim4Number = sessionManager.getSimFourPhoneNumberFromLogin();

        retrievedSim1Network = sessionManager.getNetworkNameSimOneFromLogin();
        retrievedSim2Network = sessionManager.getNetworkNameSimTwoFromLogin();
        retrievedSim3Network = sessionManager.getNetworkNameSimThreeFromLogin();
        retrievedSim4Network = sessionManager.getNetworkNameSimFourFromLogin();

        retrievedSim1Serial = sessionManager.getSimOneSerialICCIDfromLogin();
        retrievedSim2Serial = sessionManager.getSimTwoSerialICCIDfromLogin();
        retrievedSim3Serial = sessionManager.getSimThreeSerialICCIDfromLogin();
        retrievedSim4Serial = sessionManager.getSimFourSerialICCIDfromLogin();
    }

    private void initViews() {
        sim1Layout = findViewById(R.id.layout_sim1);
        sim2Layout = findViewById(R.id.layout_sim2);
        sim3Layout = findViewById(R.id.layout_sim3);
        sim4Layout = findViewById(R.id.layout_sim4);

        tvPhoneNumber1 = findViewById(R.id.tv_phone_number1);
        tvNetworkName1 = findViewById(R.id.tv_network_name1);
        imgDelete1 = findViewById(R.id.btn_delete1);
        imageNetwork1 = findViewById(R.id.img_network1);
        on_off_switch1 = findViewById(R.id.switchbutton1);

        tvPhoneNumber2 = findViewById(R.id.tv_phone_number2);
        tvNetworkName2 = findViewById(R.id.tv_network_name2);
        imgDelete2 = findViewById(R.id.btn_delete2);
        imageNetwork2 = findViewById(R.id.img_network2);
        on_off_switch2 = findViewById(R.id.switchbutton2);

        tvPhoneNumber3 = findViewById(R.id.tv_phone_number3);
        tvNetworkName3 = findViewById(R.id.tv_network_name3);
        imgDelete3 = findViewById(R.id.btn_delete3);
        imageNetwork3 = findViewById(R.id.img_network3);
        on_off_switch3 = findViewById(R.id.switchbutton3);

        tvPhoneNumber4 = findViewById(R.id.tv_phone_number4);
        tvNetworkName4 = findViewById(R.id.tv_network_name4);
        imgDelete4 = findViewById(R.id.btn_delete4);
        imageNetwork4 = findViewById(R.id.img_network4);
        on_off_switch4 = findViewById(R.id.switchbutton4);


        tvNetworkName1.setText(retrievedSim1Network);
        tvNetworkName2.setText(retrievedSim2Network);
        tvNetworkName3.setText(retrievedSim3Network);
        tvNetworkName4.setText(retrievedSim4Network);

        tvPhoneNumber1.setText(retrievedSim1Number);
        tvPhoneNumber2.setText(retrievedSim2Number);
        tvPhoneNumber3.setText(retrievedSim3Number);
        tvPhoneNumber4.setText(retrievedSim4Number);


        if (retrievedSim1Number.equalsIgnoreCase(sessionManager.getSimOnePhoneNumber())) {
            on_off_switch1.setChecked(true);
            on_off_switch1.setText("Active");

        } else if (retrievedSim2Number.equalsIgnoreCase(sessionManager.getSimOnePhoneNumber())) {
            on_off_switch2.setChecked(true);
            on_off_switch2.setText("Active");

        } else if (retrievedSim3Number.equalsIgnoreCase(sessionManager.getSimOnePhoneNumber())) {
            on_off_switch3.setChecked(true);
            on_off_switch3.setText("Active");

        } else if (retrievedSim4Number.equalsIgnoreCase(sessionManager.getSimOnePhoneNumber())) {
            on_off_switch4.setChecked(true);
            on_off_switch4.setText("Active");

        } else if (retrievedSim1Number.equalsIgnoreCase(sessionManager.getSimTwoPhoneNumber())) {
            on_off_switch1.setChecked(true);

        } else if (retrievedSim2Number.equalsIgnoreCase(sessionManager.getSimTwoPhoneNumber())) {
            on_off_switch2.setChecked(true);

        } else if (retrievedSim3Number.equalsIgnoreCase(sessionManager.getSimTwoPhoneNumber())) {
            on_off_switch3.setChecked(true);

        } else if (retrievedSim4Number.equalsIgnoreCase(sessionManager.getSimTwoPhoneNumber())) {
            on_off_switch4.setChecked(true);

        } else {
            on_off_switch3.setChecked(false);
            on_off_switch4.setEnabled(false);
        }


        if (retrievedSim1Network != null) {
            setNetworkLogo(retrievedSim1Network, imageNetwork1);
        }
        if (retrievedSim2Network != null) {
            setNetworkLogo(retrievedSim2Network, imageNetwork2);
        }
        if (retrievedSim3Network != null) {
            setNetworkLogo(retrievedSim3Network, imageNetwork3);
        }
        if (retrievedSim4Network != null) {
            setNetworkLogo(retrievedSim4Network, imageNetwork4);
        }
    }

    /**
     * set logo images based on specific network detected.
     */
    private void setNetworkLogo(String network, ImageView imageview) {
        if (network.substring(0, 3).equalsIgnoreCase("mtn")) {
            imageview.setBackgroundResource(R.drawable.mtn_logo);
        } else if (network.substring(0, 3).equalsIgnoreCase("air")) {
            imageview.setBackgroundResource(R.drawable.airtel_logo);
        } else if (network.substring(0, 3).equalsIgnoreCase("glo")) {
            imageview.setBackgroundResource(R.drawable.glo_logo);
        } else if (network.substring(0, 3).equalsIgnoreCase("9mo")
                || (network.substring(0, 3).equalsIgnoreCase("eti"))) {
            imageview.setBackgroundResource(R.drawable.nmobile_logo);
        }
    }

    private void initListeners() {




    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fade_out);
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void doLogout() {
        new Handler(Looper.getMainLooper()).post(() -> AppUtils.logOutInactivitySessionTimeout(this));
    }
}
