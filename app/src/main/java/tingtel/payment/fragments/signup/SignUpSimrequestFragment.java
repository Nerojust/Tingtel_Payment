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


public class SignUpSimrequestFragment extends Fragment {

    Button btnRegister1Sim;
    Button btnRegister2Sim;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_simrequest, container, false);

        initViews(view);
        initListeners(view);

        return view;
    }

    private void initViews(View view) {
        btnRegister1Sim = view.findViewById(R.id.btn_register_1_sim);
        btnRegister2Sim = view.findViewById(R.id.btn_register_2_sim);

        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(navhost);
    }

    private void initListeners(View view) {

        btnRegister1Sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signUpSimrequestFragment_to_signUpSim1Fragment, null);
            }
        });

        btnRegister2Sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signUpSimrequestFragment_to_signUpSim1Fragment, null);
            }
        });
    }


}
