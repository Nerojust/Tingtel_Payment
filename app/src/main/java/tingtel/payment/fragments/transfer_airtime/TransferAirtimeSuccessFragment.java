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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_airtime_success, container, false);
        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));

        initViews(view);
        initListeners();

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
        saveBeneficiary.setOnClickListener(v -> navController.navigate(R.id.action_transferAirtimeSuccessFragment_to_transferAirtimeFragment2, null));
        goToHomepageButton.setOnClickListener(v -> navController.navigate(R.id.action_transferAirtimeSuccessFragment_to_dashboardFragment, null));
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
}
