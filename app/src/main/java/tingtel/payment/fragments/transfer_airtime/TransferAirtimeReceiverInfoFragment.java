package tingtel.payment.fragments.transfer_airtime;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import tingtel.payment.utils.SessionManager;


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
    private SessionManager sessionManager;
    private Button btnPreview;
    private NavController navController;
    private ImageView whatIsPin;
    private ImageView homeImageview, settingsImagview;
    private EditText edPin;
    private EditText edReceiverPhoneNumber;
    private ImageView imgSelectBeneficiary, backButtonImageview;
    private List<NetworkSelect> networkList = new ArrayList<>();
    private String retrievedNetworkFromBottomSheet;
    private final BroadcastReceiver mas = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("selectedbeneficiary")) {
                if (intent.getAction().equalsIgnoreCase("selectedbeneficiary")) {
                    String number = intent.getStringExtra("phoneNumber");
                    retrievedNetworkFromBottomSheet = intent.getStringExtra("network");
                    edReceiverPhoneNumber.setText(AppUtils.checkPhoneNumberAndRemovePrefix(Objects.requireNonNull(number)));
                    setReceiversNetworkRv(retrievedNetworkFromBottomSheet);
                    bottomSheetFragment.dismiss();

                }
            }
        }
    };

    private void setReceiversNetworkRv(String selectedNetwork) {


        if (selectedNetwork != null) {

            int pos = -1;
            if (selectedNetwork.equalsIgnoreCase("MTN")) {
                pos = 0;
            } else if (selectedNetwork.equalsIgnoreCase("9MOBILE")) {
                pos = 1;
            } else if (selectedNetwork.equalsIgnoreCase("AIRTEL")) {
                pos = 2;
            } else if (selectedNetwork.equalsIgnoreCase("GLO")) {
                pos = 3;
            }

            int finalPos = pos;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (finalPos != -1) {
                        recyclerView.findViewHolderForAdapterPosition(finalPos).itemView.performClick();
                    }
                }
            }, 5);
        }
    }

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
        sessionManager = AppUtils.getSessionManagerInstance();
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
        edPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4 && !edReceiverPhoneNumber.getText().toString().isEmpty()) {
                    AppUtils.changeStatusOfButton(Objects.requireNonNull(getContext()), btnPreview, true);
                } else {
                    AppUtils.changeStatusOfButton(Objects.requireNonNull(getContext()), btnPreview, false);
                }
            }
        });
        whatIsPin = view.findViewById(R.id.whatIsPin_id);
        edReceiverPhoneNumber = view.findViewById(R.id.receivers_phone_number_edittext);
        imgSelectBeneficiary = view.findViewById(R.id.img_beneficiary);

        recyclerView = view.findViewById(R.id.recycler_view);

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }


    private boolean isValidFields() {
        String network = sessionManager.getSelectedRvNetwork();
        if (edReceiverPhoneNumber.getText().toString().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.enter_receivers_number), edReceiverPhoneNumber);
            edReceiverPhoneNumber.requestFocus();
            return false;
        }
        if (edReceiverPhoneNumber.getText().toString().length() < Constants.MINIMUM_PHONE_NUMBER_DIGITS) {
            AppUtils.showSnackBar(getResources().getString(R.string.number_too_short), edReceiverPhoneNumber);
            edReceiverPhoneNumber.requestFocus();
            return false;
        }

        if (network.equals("")) {
            AppUtils.showSnackBar(getResources().getString(R.string.select_a_network), edPin);
            return false;
        }
        if (edPin.getText().toString().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.enter_network_pin), edPin);
            edPin.requestFocus();
            return false;
        }
        if (edPin.getText().toString().length() < 4) {
            AppUtils.showSnackBar(getResources().getString(R.string.pin_too_short), edPin);
            edPin.requestFocus();
            return false;
        }
        return true;
    }
}
