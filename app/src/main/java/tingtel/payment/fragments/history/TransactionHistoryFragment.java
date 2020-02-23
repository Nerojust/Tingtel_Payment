package tingtel.payment.fragments.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import tingtel.payment.MainActivity;
import tingtel.payment.R;
import tingtel.payment.activities.SettingsActivity;


public class TransactionHistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
//        ((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle("History");

        LinearLayout backButtonLayout = view.findViewById(R.id.backArrowLayout);
        ImageView homeImageview = view.findViewById(R.id.homeImageview);
        ImageView settingsImagview = view.findViewById(R.id.settingsImageview);

        backButtonLayout.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        homeImageview.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        settingsImagview.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));


        return view;
    }
}
