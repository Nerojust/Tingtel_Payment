package tingtel.payment.fragments.dashboard;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tingtel.payment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferAirtimeSuccessFragment extends Fragment {


    public TransferAirtimeSuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transfer_airtime_success, container, false);
    }

}
