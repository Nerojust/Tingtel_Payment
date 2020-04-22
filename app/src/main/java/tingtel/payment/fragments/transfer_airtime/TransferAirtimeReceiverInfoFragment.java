package tingtel.payment.fragments.transfer_airtime;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.adapters.NetworkSelectAdapter;
import tingtel.payment.models.NetworkSelect;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.Constants;


public class TransferAirtimeReceiverInfoFragment extends Fragment {
    final String[] permissions = new String[]{
            Manifest.permission.READ_CONTACTS
    };
    String[] spinnerPopulation;
    private SelectBeneficiarySheetFragment bottomSheetFragment;
    private NetworkSelectAdapter adapter;
    private RecyclerView recyclerView;
    private String[] spinnerTitles;
    private int[] spinnerImages;
    private String SenderSimNetwork;
    private String ReceiverSimNetwork;
    private int SimNo;
    private String Amount;
    private String SimSerial;
    private Button btnPreview;
    private NavController navController;
    private ImageView whatIsPin;
    private ImageView homeImageview, settingsImagview;
    private EditText edPin;
    private EditText edReceiverPhoneNumber;
    private final BroadcastReceiver mas = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("barcodeSerialcaptured")) {
                if (intent.getAction().equalsIgnoreCase("barcodeSerialcaptured")) {
                    edReceiverPhoneNumber.setText(AppUtils.getSessionManagerInstance().getScannedCodeResult());
                }
            }

            if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("selectedbeneficiary")) {
                if (intent.getAction().equalsIgnoreCase("selectedbeneficiary")) {
                    String number = intent.getStringExtra("phoneNumber");
                    edReceiverPhoneNumber.setText(AppUtils.checkPhoneNumberAndRemovePrefix(number));
                    bottomSheetFragment.dismiss();

                }
            }
        }
    };

    private ImageView imgSelectBeneficiary, backButtonImageview;
    private List<NetworkSelect> networkList = new ArrayList<>();

    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(this.mas, new IntentFilter("barcodeSerialcaptured"));
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(this.mas, new IntentFilter("selectedbeneficiary"));
    }

    public void onDestroy() {
        //softInputAssist.onDestroy();
        super.onDestroy();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(this.mas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_airtime_receiver_info, container, false);

        getExtrasFromIntent();
        initViews(view);
        initListeners();
        initRv();

        return view;
    }

    private void initRv() {
        adapter = new NetworkSelectAdapter(getContext(), networkList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        networkList.clear();
        prepareNetworkData();
    }

    private void prepareNetworkData() {
        NetworkSelect networkSelect = new NetworkSelect(R.drawable.mtnlogo, "MTN");
        networkList.add(networkSelect);

        networkSelect = new NetworkSelect(R.drawable.nmobilelogo, "9MOBILE");
        networkList.add(networkSelect);

        networkSelect = new NetworkSelect(R.drawable.airtellogo, "AIRTEL");
        networkList.add(networkSelect);

        networkSelect = new NetworkSelect(R.drawable.glologo, "GLO");
        networkList.add(networkSelect);

        adapter.notifyDataSetChanged();
    }

    private void initListeners() {
        backButtonImageview.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        homeImageview.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        });
        settingsImagview.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));

        imgSelectBeneficiary.setOnClickListener(v -> {

            bottomSheetFragment = new SelectBeneficiarySheetFragment();
            bottomSheetFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheetFragment.getTag());

        });

        btnPreview.setOnClickListener(v -> {
            if (isValidFields()) {
                ReceiverSimNetwork = AppUtils.getSessionManagerInstance().getSelectedRvNetwork();
                String number = AppUtils.checkPhoneNumberAndRestructure(edReceiverPhoneNumber.getText().toString().trim());
                Bundle bundle = new Bundle();
                bundle.putString("senderSimNetwork", SenderSimNetwork);
                bundle.putString("receiverSimNetwork", ReceiverSimNetwork);
                bundle.putString("simSerial", SimSerial);
                bundle.putInt("simNo", SimNo);
                bundle.putString("amount", Amount);
                bundle.putString("receiverPhoneNumber", number);
                bundle.putString("pin", edPin.getText().toString());
                navController.navigate(R.id.action_transferAirtimeReceiverInfoFragment2_to_transferAirtimePreviewFragment2, bundle);
            }
        });

        whatIsPin.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("network", SenderSimNetwork);
            navController.navigate(R.id.action_transferAirtimeReceiverInfoFragment2_to_getTransferPinTutorialFragment2, bundle);
        });

    }

    private void getExtrasFromIntent() {
        SenderSimNetwork = Objects.requireNonNull(getArguments()).getString("simNetwork");
        SimNo = getArguments().getInt("simNo");
        Amount = getArguments().getString("amount");
        SimSerial = getArguments().getString("simSerial");
    }

    private void initViews(View view) {
        AppUtils.getSessionManagerInstance().setSelectedRvNetwork("");

        AppUtils.showProgressTracker(view, Objects.requireNonNull(getContext()));

        backButtonImageview = view.findViewById(R.id.backArrowLayout);
        homeImageview = view.findViewById(R.id.homeImageview);
        settingsImagview = view.findViewById(R.id.settingsImageview);


        btnPreview = view.findViewById(R.id.btn_preview);

        edPin = view.findViewById(R.id.pinEditext);
        whatIsPin = view.findViewById(R.id.whatIsPin_id);
        edReceiverPhoneNumber = view.findViewById(R.id.receivers_phone_number_edittext);
        imgSelectBeneficiary = view.findViewById(R.id.img_beneficiary);


        recyclerView = view.findViewById(R.id.recycler_view);

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));


    }


    private boolean isValidFields() {
        String network = AppUtils.getSessionManagerInstance().getSelectedRvNetwork();
        if (edReceiverPhoneNumber.getText().toString().isEmpty()) {
            AppUtils.showSnackBar("Enter receivers number", edReceiverPhoneNumber);
            edReceiverPhoneNumber.requestFocus();
            return false;
        }
        if (edReceiverPhoneNumber.getText().toString().length() < Constants.MINIMUM_PHONE_NUMBER_DIGITS) {
            AppUtils.showSnackBar("Number is too short. Must be 11 digits", edReceiverPhoneNumber);
            edReceiverPhoneNumber.requestFocus();
            return false;
        }

        if (network.equals("")) {
            AppUtils.showSnackBar("Select a network", edPin);
            return false;
        }
        if (edPin.getText().toString().isEmpty()) {
            AppUtils.showSnackBar("Enter network pin", edPin);
            edPin.requestFocus();
            return false;
        }
        if (edPin.getText().toString().length() < 4) {
            AppUtils.showSnackBar("Pin is too short", edPin);
            edPin.requestFocus();
            return false;
        }
        return true;
    }
}
