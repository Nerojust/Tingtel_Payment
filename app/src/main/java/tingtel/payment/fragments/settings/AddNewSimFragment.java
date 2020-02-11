package tingtel.payment.fragments.settings;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tingtel.payment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewSimFragment extends Fragment {


    public AddNewSimFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_sim, container, false);
    }

}
