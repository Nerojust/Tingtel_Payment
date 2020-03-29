package tingtel.payment.fragments.transfer_airtime;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.history.StatusActivity;
import tingtel.payment.activities.settings.SettingsActivity;
import tingtel.payment.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferAirtimeSuccessFragment extends Fragment {

    private Button statusButton, saveBeneficiary, checkBalanceButton;
    private NavController navController;

    private ImageView homeImageview, settingsImagview;
    private ImageView backButtonImageview;
    private String SenderSimNetwork;
    private String SenderPhoneNumber;
    private String ReceiverSimNetwork;
    private String ReceiverPhoneNumber;
    private int SimNo;
    private String Amount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_airtime_success, container, false);
        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));

        initViews(view);
        initListeners();
        getExtrasFromIntent();

        return view;
    }

    private void initListeners() {

        backButtonImageview.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        homeImageview.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();

        });

        settingsImagview.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));

        checkBalanceButton.setOnClickListener(v -> checkBalance());

        statusButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),StatusActivity.class);
            intent.putExtra("simNo", SimNo);
            startActivity(intent);
        });

        saveBeneficiary.setOnClickListener(v -> {
            SaveBeneficiarySheetFragment bottomSheetFragment = new SaveBeneficiarySheetFragment(getActivity());
            Bundle bundle = new Bundle();
            bundle.putString("ReceiverPhoneNumber", ReceiverPhoneNumber);
            bundle.putString("ReceiverNetwork", ReceiverSimNetwork);
            bottomSheetFragment.setArguments(bundle);
            bottomSheetFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
    }

    /**
     * get detail from intent
     */
    private void getExtrasFromIntent() {
        SenderSimNetwork = Objects.requireNonNull(getArguments()).getString("senderSimNetwork");
        ReceiverSimNetwork = getArguments().getString("receiverSimNetwork");
        SimNo = getArguments().getInt("simNo");
        Amount = getArguments().getString("amount");
        ReceiverPhoneNumber = getArguments().getString("receiverPhoneNumber");

    }

    private void checkBalance() {
        String UssdCode;
        if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("mtn")) {
            UssdCode = "*556#";
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("air")) {
            UssdCode = "*123#";
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("glo")) {
            UssdCode = "*124*1#";
        } else if (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("9mo") ||
                (SenderSimNetwork.substring(0, 3).equalsIgnoreCase("eti"))) {
            UssdCode = "*232#";
        } else {

            Toast.makeText(getActivity(), "Cant Check USSD Balance for this network", Toast.LENGTH_LONG).show();
            return;
        }
        statusButton.setBackground(getResources().getDrawable(R.drawable.dashboard_buttons));
        statusButton.setEnabled(true);
        AppUtils.dialUssdCode(getActivity(), UssdCode, SimNo);
    }

    private void initViews(View view) {
        backButtonImageview = view.findViewById(R.id.backArrowLayout);
        homeImageview = view.findViewById(R.id.homeImageview);
        settingsImagview = view.findViewById(R.id.settingsImageview);

        checkBalanceButton = view.findViewById(R.id.check_balance_button);
        statusButton = view.findViewById(R.id.status_button);
        saveBeneficiary = view.findViewById(R.id.makeAnotherTransferButton);

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRemoving()) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }
}
