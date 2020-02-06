package tingtel.payment.fragments.signup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tingtel.payment.R;


public class SIgnUpSim1SuccessFragment extends Fragment {

Button btnRegSim2Now;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;


        view = inflater.inflate(R.layout.fragment_sign_up_sim1_success, container, false);

        initViews(view);
        initListeners(view);

        return view;
    }

    private void initListeners(View view) {

        btnRegSim2Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_SIgnUpSim1SuccessFragment_to_signUpSim2Fragment, null);
            }
        });
    }

    private void initViews(View view) {
        btnRegSim2Now = view.findViewById(R.id.btn_reg_sim2_now);

        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(navhost);
    }


}
