package tingtel.payment.fragments.signup;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.sign_in.SignInActivity;
import tingtel.payment.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpHomeFragment extends Fragment {

    private Button btnSignUp;
    private NavController navController;
    private TextView tvLogin;
    private TextInputEditText firstName, lastName, emailAddress, username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_home, container, false);

        initViews(view);
        initListeners(view);
        return view;

    }


    private void initListeners(View view) {
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            Objects.requireNonNull(getActivity()).startActivity(intent);
        });


        btnSignUp.setOnClickListener(v -> navController.navigate(R.id.action_signUpHomeFragment_to_signUpSim1Fragment, null));
    }

    private void initViews(View view) {
        firstName = view.findViewById(R.id.tv_first_name);
        lastName = view.findViewById(R.id.tv_last_name);
        emailAddress = view.findViewById(R.id.tv_email_address);
        username = view.findViewById(R.id.tv_username);

        tvLogin = view.findViewById(R.id.tv_login);
        btnSignUp = view.findViewById(R.id.btn_sign_up);

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
    }

    private boolean isValidFields() {
        if (firstName.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "First name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (firstName.getText().toString().trim().length() <3){
            Toast.makeText(getContext(), "First name is too short", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (lastName.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Last name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (lastName.getText().toString().trim().length() <3){
            Toast.makeText(getContext(), "Last name is too short", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (emailAddress.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(emailAddress.getText().toString().trim())){
            Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (username.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.getText().toString().trim().length() <3){
            Toast.makeText(getContext(), "Username is too short", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
