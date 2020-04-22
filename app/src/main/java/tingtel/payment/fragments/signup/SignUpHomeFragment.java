package tingtel.payment.fragments.signup;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.activities.sign_in.SignInActivity;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpHomeFragment extends Fragment {

    private Button btnSignUp;
    private NavController navController;
    private TextView tvLogin;
    private TextInputEditText firstName, lastName, emailAddress, username;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_home, container, false);
        sessionManager = AppUtils.getSessionManagerInstance();

        initViews(view);
        initListeners();
        return view;

    }

    private void initListeners() {
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            Objects.requireNonNull(getActivity()).startActivity(intent);
        });


        btnSignUp.setOnClickListener(v -> {
            if (isValidFields()) {
                saveDatatoPrefs();

                navController.navigate(R.id.action_signUpHomeFragment_to_signUpSim1Fragment, null);
            }
        });
    }

    private void saveDatatoPrefs() {
        sessionManager.setFirstNameFromLogin(Objects.requireNonNull(firstName.getText()).toString().trim());
        sessionManager.setLastName(Objects.requireNonNull(lastName.getText()).toString().trim());
        sessionManager.setEmailAddress(Objects.requireNonNull(emailAddress.getText()).toString().trim());
        sessionManager.setUsername(Objects.requireNonNull(username.getText()).toString().trim());
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
        if (firstName.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.first_name_is_required), firstName);
            firstName.requestFocus();
            return false;
        }
        if (firstName.getText().toString().trim().length() < 3) {
            AppUtils.showSnackBar(getResources().getString(R.string.name_is_too_short), firstName);
            firstName.requestFocus();
            return false;
        }
        if (lastName.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.last_name_is_required), lastName);
            lastName.requestFocus();
            return false;
        }
        if (lastName.getText().toString().trim().length() < 3) {
            AppUtils.showSnackBar(getResources().getString(R.string.name_is_too_short), lastName);
            lastName.requestFocus();
            return false;
        }
        if (emailAddress.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.email_is_required), emailAddress);
            emailAddress.requestFocus();
            return false;
        }
        if (!AppUtils.isValidEmailAddress(emailAddress.getText().toString().trim())) {
            AppUtils.showSnackBar(getResources().getString(R.string.invalid_email_address), emailAddress);
            emailAddress.requestFocus();
            return false;
        }

        if (username.getText().toString().trim().isEmpty()) {
            AppUtils.showSnackBar(getResources().getString(R.string.username_is_required), username);
            username.requestFocus();
            return false;
        }
        if (username.getText().toString().trim().length() < 3) {
            AppUtils.showSnackBar(getResources().getString(R.string.name_is_too_short), username);
            username.requestFocus();
            return false;
        }

        return true;
    }

}
