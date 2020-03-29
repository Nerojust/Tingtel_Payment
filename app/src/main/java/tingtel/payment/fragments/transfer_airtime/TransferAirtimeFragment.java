package tingtel.payment.fragments.transfer_airtime;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.DecimalFormat;
import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.activities.sign_up.SignUpActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.AppUtils.dialUssdCode;


public class TransferAirtimeFragment extends Fragment {

    TextView tvGetTransferPin;
    private Button btnCheckBalance;
    private EditText edAmount;
    private Button btnNext;
    private NavController navController;
    private String UssdCode;
    private String SimNetwork;
    private int SimNo;
    private String SimSerial;
    private SessionManager sessionManager;
    private AppDatabase appDatabase;
    private ImageView homeImageview, settingsImagview, backButtonImageview;
    private TextView sim1Textview, sim2Textview;
    private String finalamount;
    private Boolean balanceChecked = false;
    private boolean isSim1TextviewClicked = false;
    private boolean isSim2TextviewClicked = false;
    private String noOfSIm;
    // private SoftInputAssist softInputAssist;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_airtime, container, false);

        initViews(view);
        initListeners(view);

        confirmSimRegistrations();
        populateSimRadioButtons();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    //For handling item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Toast.makeText(getContext(), "home Fragment item", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews(View view) {
        //softInputAssist = new SoftInputAssist(getActivity());

        String[] descriptionData = {"Sender", "Receiver", "Summary"};
        StateProgressBar stateProgressBar = view.findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateDescriptionTypeface("font/rubik_regular.ttf");
        stateProgressBar.setStateNumberTypeface("font/rubik_regular.ttf");

        backButtonImageview = view.findViewById(R.id.backArrowLayout);
        homeImageview = view.findViewById(R.id.homeImageview);
        settingsImagview = view.findViewById(R.id.settingsImageview);
        sessionManager = AppUtils.getSessionManagerInstance();
        sim1Textview = view.findViewById(R.id.sim_one_textview);
        sim2Textview = view.findViewById(R.id.sim_two_textview);

        sim1Textview.setBackground(getResources().getDrawable(R.drawable.sim_corners_left));
        sim1Textview.setTextColor(getResources().getColor(R.color.tingtel_red_color));

        sim2Textview.setTextColor(getResources().getColor(R.color.tingtel_red_color));
        sim2Textview.setBackground(getResources().getDrawable(R.drawable.sim_corners_right2));

        btnCheckBalance = view.findViewById(R.id.check_balance_button);
        btnNext = view.findViewById(R.id.next_button);
        edAmount = view.findViewById(R.id.amount_edittext);
        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String input = s.toString();

                if (!input.isEmpty()) {

                    input = input.replace(",", "");

                    DecimalFormat format = new DecimalFormat("#,###,###");
                    String newPrice = format.format(Double.parseDouble(input));

                    edAmount.removeTextChangedListener(this); //To Prevent from Infinite Loop

                    edAmount.setText(newPrice);
                    edAmount.setSelection(newPrice.length()); //Move Cursor to end of String

                    edAmount.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
            }
        });

        appDatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getContext()));

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }

    private void initListeners(View view) {
        backButtonImageview.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        homeImageview.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();

        });

        settingsImagview.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));

        sim1Textview.setOnClickListener(v -> {
            isSim1TextviewClicked = true;
            isSim2TextviewClicked = false;
            if (noOfSIm.equalsIgnoreCase("SIM1")) {
                sim1Textview.setBackground(getResources().getDrawable(R.drawable.sim_full_red));
                sim1Textview.setTextColor(getResources().getColor(R.color.white));

            } else if (noOfSIm.equalsIgnoreCase("SIM1 SIM2")) {
                sim1Textview.setBackground(getResources().getDrawable(R.drawable.sim_corners_left2));
                sim1Textview.setTextColor(getResources().getColor(R.color.white));

                sim2Textview.setTextColor(getResources().getColor(R.color.tingtel_red_color));
                sim2Textview.setBackground(getResources().getDrawable(R.drawable.sim_corners_right2));
            }
        });

        sim2Textview.setOnClickListener(v -> {
            isSim2TextviewClicked = true;
            isSim1TextviewClicked = false;
            sim2Textview.setBackground(getResources().getDrawable(R.drawable.sim_corners_right));
            sim2Textview.setTextColor(getResources().getColor(R.color.white));

            sim1Textview.setTextColor(getResources().getColor(R.color.tingtel_red_color));
            sim1Textview.setBackground(getResources().getDrawable(R.drawable.sim_corners_left));
        });


        btnCheckBalance.setOnClickListener(v -> {

            if (!isSim1TextviewClicked && !isSim2TextviewClicked) {
                AppUtils.showSnackBar("Select a number and check balance", sim1Textview);
                return;
            }

            performCheckBeforeDialing();
        });


        btnNext.setOnClickListener(v -> {
            //if all fields and conditions are satisfied proceed.
            if (isValidAllFields()) {
                String input = edAmount.getText().toString().trim();
                finalamount = input.replace(",", "");

                Bundle bundle = new Bundle();
                bundle.putString("simNetwork", "" + SimNetwork);
                bundle.putString("simSerial", "" + SimSerial);
                bundle.putInt("simNo", SimNo);
                bundle.putString("amount", finalamount);
                navController.navigate(R.id.action_transferAirtimeFragment2_to_transferAirtimeReceiverInfoFragment2, bundle);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSim1TextviewClicked) {
            if (sessionManager.getClickedNetwork() != null) {
                sim1Textview.setBackground(getResources().getDrawable(R.drawable.sim_corners_left2));
                sim1Textview.setTextColor(getResources().getColor(R.color.white));
            }
        } else if (isSim2TextviewClicked) {
            sim2Textview.setBackground(getResources().getDrawable(R.drawable.sim_corners_right));
            sim2Textview.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onResume() {
//        softInputAssist.onResume();
        super.onResume();
    }

    private void performCheckBeforeDialing() {
        if (isSim1TextviewClicked) {
            if (sim1Textview.getText().toString().substring(0, 3).equalsIgnoreCase("mtn")) {
                SimNetwork = "Mtn";
                UssdCode = "*556#";
                sessionManager.setClickedNetwork("mtn");
            } else if (sim1Textview.getText().toString().substring(0, 3).equalsIgnoreCase("air")) {
                SimNetwork = "Airtel";
                UssdCode = "*123#";
                sessionManager.setClickedNetwork("airtel");
            } else if (sim1Textview.getText().toString().substring(0, 3).equalsIgnoreCase("glo")) {
                SimNetwork = "Glo";
                UssdCode = "*124*1#";
                sessionManager.setClickedNetwork("glo");
            } else if (sim1Textview.getText().toString().substring(0, 3).equalsIgnoreCase("9mo") ||
                    (sim1Textview.getText().toString().substring(0, 3).equalsIgnoreCase("eti"))) {
                SimNetwork = "9mobile";
                UssdCode = "*232#";
                sessionManager.setClickedNetwork("eti");
            } else {
                Toast.makeText(getActivity(), "Cant Check USSD Balance for this network", Toast.LENGTH_LONG).show();
                return;
            }

            SimNo = 0;
            SimSerial = sessionManager.getSimSerialICCID();

            dialUssdCode(getActivity(), UssdCode, SimNo);
            balanceChecked = true;

        } else if (isSim2TextviewClicked) {
            if (sim2Textview.getText().toString().substring(0, 3).equalsIgnoreCase("mtn")) {
                SimNetwork = "Mtn";
                UssdCode = "*556#";
            } else if (sim2Textview.getText().toString().substring(0, 3).equalsIgnoreCase("air")) {
                SimNetwork = "Airtel";
                UssdCode = "*123#";
            } else if (sim2Textview.getText().toString().substring(0, 3).equalsIgnoreCase("glo")) {
                SimNetwork = "Glo";
                UssdCode = "*124*1#";
            } else if (sim2Textview.getText().toString().substring(0, 3).equalsIgnoreCase("9mo") ||
                    (sim2Textview.getText().toString().substring(0, 3).equalsIgnoreCase("eti"))) {
                SimNetwork = "9mobile";
                UssdCode = "*232#";
            } else {
                Toast.makeText(getActivity(), "Cant Check USSD Balance for this network", Toast.LENGTH_LONG).show();
                return;
            }

            SimNo = 1;

            SimSerial = sessionManager.getSimSerialICCID1();

            dialUssdCode(getActivity(), UssdCode, SimNo);
            balanceChecked = true;

        } else {
            Toast.makeText(getContext(), "Click on a number first", Toast.LENGTH_SHORT).show();
        }
        sessionManager.setWhichSimWasClicked(SimNo);
    }

    private void confirmSimRegistrations() {
        String NoOfSIm = sessionManager.getSimStatus();

        switch (NoOfSIm) {

            case "NO SIM":
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                intent.putExtra("task", "registerSim1");
                startActivity(intent);

                break;

            case "SIM1":

                if (!sim1ExistsCheck()) {
                    Toast.makeText(getActivity(), "New Sim Detected, You Need to Register this sim on your account", Toast.LENGTH_LONG).show();
                    // navigateToSim1Register();
                    Intent intent2 = new Intent(getActivity(), SignUpActivity.class);
                    intent2.putExtra("task", "registerSim1");
                    startActivity(intent2);
                    return;
                }
                break;
            case "SIM1 SIM2":

                if (!sim1ExistsCheck()) {
                    Toast.makeText(getActivity(), "New Sim Detected, You Need to Register this sim on your account", Toast.LENGTH_LONG).show();
                    // navigateToSim1Register();
                    Intent intent3 = new Intent(getActivity(), SignUpActivity.class);
                    intent3.putExtra("task", "registerSim1");
                    startActivity(intent3);
                    return;
                }

                if (!sim2ExistsCheck()) {
                    Toast.makeText(getActivity(), "New Sim Detected, You Need to Register this sim on your account", Toast.LENGTH_LONG).show();
                    // navigateToSim2Register();
                    Intent intent4 = new Intent(getActivity(), SignUpActivity.class);
                    intent4.putExtra("task", "registerSim2");
                    startActivity(intent4);
                    return;
                }
                break;
        }
    }


    private boolean sim1ExistsCheck() {
        String Sim1Serial = sessionManager.getSimSerialICCID();
        return appDatabase.simCardsDao().getSerial(Sim1Serial).size() > 0;
    }

    private boolean sim2ExistsCheck() {
        String Sim2Serial = sessionManager.getSimSerialICCID1();
        return appDatabase.simCardsDao().getSerial(Sim2Serial).size() > 0;
    }


    private void populateSimRadioButtons() {
        noOfSIm = sessionManager.getSimStatus();

        switch (noOfSIm) {
            case "NO SIM":
                sim1Textview.setVisibility(View.GONE);
                sim2Textview.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Insert a sim card", Toast.LENGTH_SHORT).show();
                break;
            case "SIM1":
                sim1Textview.setVisibility(View.VISIBLE);
                sim2Textview.setVisibility(View.GONE);
                sim1Textview.setText(sessionManager.getNetworkName() + "\n" + sessionManager.getSimPhoneNumber());

                sim1Textview.setBackground(getResources().getDrawable(R.drawable.sim_full_white));
                sim1Textview.setTextColor(getResources().getColor(R.color.tingtel_red_color));

                break;

            case "SIM1 SIM2":
                sim1Textview.setVisibility(View.VISIBLE);
                sim2Textview.setVisibility(View.VISIBLE);
                sim1Textview.setText(sessionManager.getNetworkName() + "\n" + sessionManager.getSimPhoneNumber());
                sim2Textview.setText(sessionManager.getNetworkName1() + "\n" + sessionManager.getSimPhoneNumber1());

                sim1Textview.setBackground(getResources().getDrawable(R.drawable.sim_corners_left));
                sim1Textview.setTextColor(getResources().getColor(R.color.tingtel_red_color));

                sim2Textview.setTextColor(getResources().getColor(R.color.tingtel_red_color));
                sim2Textview.setBackground(getResources().getDrawable(R.drawable.sim_corners_right2));

                break;
        }
    }

    private boolean isValidAllFields() {
        if (!isSim1TextviewClicked && !isSim2TextviewClicked) {
            AppUtils.showSnackBar("Select a number", sim1Textview);
            return false;
        }

        if (edAmount.getText().toString().isEmpty()) {
            AppUtils.showSnackBar("Amount is required", edAmount);
            edAmount.requestFocus();
            return false;
        }
        if (edAmount.getText().toString().trim().length() < 3) {
            AppUtils.showSnackBar("Amount is too low. Minimum is " + getResources().getString(R.string.naira) + "100", edAmount);
            edAmount.requestFocus();
            return false;
        }

        //todo: add not
        if (!balanceChecked) {
            AppUtils.showSnackBar("Please Check Account Balance First", btnCheckBalance);
            return false;
        }

        return true;
    }
}
