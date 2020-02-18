package tingtel.payment.fragments.signup;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import tingtel.payment.R;
import tingtel.payment.SignInActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpHomeFragment extends Fragment {

    private TextView tvLogin;
    Button btnSignUp;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sign_up_home, container, false);

        initViews(view);
        initListeners(view);
        return  view;

    }

    private void initListeners(View view) {

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                getActivity().startActivity(intent);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signUpHomeFragment_to_signUpSim1Fragment, null);
            }
        });
    }

    private void initViews(View view) {

        tvLogin = view.findViewById(R.id.tv_login);
        btnSignUp = view.findViewById(R.id.btn_sign_up);

        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(navhost);

    }
}
