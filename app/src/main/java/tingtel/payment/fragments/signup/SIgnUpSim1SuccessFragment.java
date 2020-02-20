package tingtel.payment.fragments.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import tingtel.payment.R;


public class SIgnUpSim1SuccessFragment extends Fragment {

    private Button btnRegSim2Now;
    private Button btnRegSim2Later;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_sim1_success, container, false);

        initViews(view);
        initListeners(view);

        return view;
    }

    private void initListeners(View view) {

        btnRegSim2Now.setOnClickListener(v -> navController.navigate(R.id.action_SIgnUpSim1SuccessFragment_to_signUpSim2Fragment, null));

        btnRegSim2Later.setOnClickListener(v -> navController.navigate(R.id.action_SIgnUpSim1SuccessFragment_to_setPasswordFragment, null));
    }

    private void initViews(View view) {
        btnRegSim2Now = view.findViewById(R.id.btn_reg_sim2_now);
        btnRegSim2Later = view.findViewById(R.id.btn_reg_sim2_later);

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }
}
