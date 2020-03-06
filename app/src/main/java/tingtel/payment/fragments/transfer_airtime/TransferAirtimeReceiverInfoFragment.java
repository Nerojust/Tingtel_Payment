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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import tingtel.payment.adapters.SpinnerAdapter;
import tingtel.payment.models.NetworkSelect;
import tingtel.payment.utils.AppUtils;


public class TransferAirtimeReceiverInfoFragment extends Fragment {
    final String[] permissions = new String[]{
            Manifest.permission.READ_CONTACTS
    };
    String[] spinnerPopulation;
    SelectBeneficiarySheetFragment bottomSheetFragment;
    NetworkSelectAdapter adapter;
    RecyclerView recyclerView;
    private String[] spinnerTitles;
    private int[] spinnerImages;
    private String SenderSimNetwork;
    private String ReceiverSimNetwork;
    private int SimNo;
    private String Amount;
    private String SimSerial;
    private Button btnPreview;
    private NavController navController;
    private TextView whatIsPin;
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
                    Toast.makeText(getActivity(), "received", Toast.LENGTH_LONG).show();
                    edReceiverPhoneNumber.setText(intent.getStringExtra("phoneNumber"));
                    bottomSheetFragment.dismiss();

                }
            }
        }
    };

    private ImageView imgSelectBeneficiary, backButtonImageview;
    private List<NetworkSelect> networkList = new ArrayList<>();
    private SpinnerAdapter mCustomAdapter;
    private Spinner mSpinner;

    public void onResume() {
        super.onResume();
        //  Log.e(TAG, "onResume()");
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(this.mas, new IntentFilter("barcodeSerialcaptured"));
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(this.mas, new IntentFilter("selectedbeneficiary"));
    }

    public void onDestroy() {
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
            bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());

        });

        btnPreview.setOnClickListener(v -> {
            ReceiverSimNetwork = AppUtils.getSessionManagerInstance().getSelectedRvNetwork();

            Bundle bundle = new Bundle();
            bundle.putString("senderSimNetwork", SenderSimNetwork);
            bundle.putString("receiverSimNetwork", ReceiverSimNetwork);
            bundle.putString("simSerial", SimSerial);
            bundle.putInt("simNo", SimNo);
            bundle.putString("amount", Amount);
            bundle.putString("receiverPhoneNumber", edReceiverPhoneNumber.getText().toString());
            bundle.putString("pin", edPin.getText().toString());
            navController.navigate(R.id.action_transferAirtimeReceiverInfoFragment2_to_transferAirtimePreviewFragment2, bundle);

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
        backButtonImageview = view.findViewById(R.id.backArrowLayout);
        homeImageview = view.findViewById(R.id.homeImageview);
        settingsImagview = view.findViewById(R.id.settingsImageview);


        btnPreview = view.findViewById(R.id.btn_preview);

        edPin = view.findViewById(R.id.pinEditext);
        whatIsPin = view.findViewById(R.id.whatIsPin_id);
        edReceiverPhoneNumber = view.findViewById(R.id.receivers_phone_number_edittext);
        imgSelectBeneficiary = view.findViewById(R.id.img_beneficiary);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));


    }


    private boolean isValidFields() {
        if (edReceiverPhoneNumber.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter the receivers number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edPin.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter the receivers number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edReceiverPhoneNumber.getText().toString().length() < 11) {
            Toast.makeText(getContext(), "Phone number is too short", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edPin.getText().toString().length() < 4) {
            Toast.makeText(getContext(), "Pin is too short", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
