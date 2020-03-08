package tingtel.payment.fragments.transfer_airtime;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.history.StatusActivity;
import tingtel.payment.activities.settings.SettingsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferAirtimeSuccessFragment extends Fragment {

    private Button statusButton, saveBeneficiary, goToHomepageButton;
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

        statusButton.setOnClickListener(v -> startActivity(new Intent(getContext(), StatusActivity.class)));

        saveBeneficiary.setOnClickListener(v -> {

            SaveBeneficiarySheetFragment bottomSheetFragment = new SaveBeneficiarySheetFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ReceiverPhoneNumber", ReceiverPhoneNumber);
            bundle.putString("ReceiverNetwork", ReceiverSimNetwork);
            bottomSheetFragment.setArguments(bundle);
            bottomSheetFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheetFragment.getTag());

            //navController.navigate(R.id.action_transferAirtimeSuccessFragment_to_transferAirtimeFragment2, null);
        });

        goToHomepageButton.setOnClickListener(v -> navController.navigate(R.id.action_transferAirtimeSuccessFragment_to_dashboardFragment, null));
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

    private void initViews(View view) {
        backButtonImageview = view.findViewById(R.id.backArrowLayout);
        homeImageview = view.findViewById(R.id.homeImageview);
        settingsImagview = view.findViewById(R.id.settingsImageview);

        statusButton = view.findViewById(R.id.status_button);
        saveBeneficiary = view.findViewById(R.id.makeAnotherTransferButton);
        goToHomepageButton = view.findViewById(R.id.goToHomePageButton);

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
