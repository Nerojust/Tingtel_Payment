package tingtel.payment.fragments.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import tingtel.payment.R;


public class TransactionHistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
//        ((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle("History");

        return view;
    }
}
