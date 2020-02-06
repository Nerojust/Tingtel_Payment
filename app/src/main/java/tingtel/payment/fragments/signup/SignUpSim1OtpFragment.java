package tingtel.payment.fragments.signup;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tingtel.payment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpSim1OtpFragment extends Fragment {


   Button btnConfirmOtp;
    NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_sim1_otp, container, false);

        initViews(view);
        initListeners(view);

        return view;
    }

    private void initListeners(View view) {

        btnConfirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signUpSim1OtpFragment_to_SIgnUpSim1SuccessFragment, null);
            }
        });

    }

    private void initViews(View view) {

        btnConfirmOtp = view.findViewById(R.id.btn_confirm_otp);

        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(navhost);

    }

}
