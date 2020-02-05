package tingtel.payment.fragments.signup;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tingtel.payment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpHomeFragment extends Fragment {


    public SignUpHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_home, container, false);
    }

}
