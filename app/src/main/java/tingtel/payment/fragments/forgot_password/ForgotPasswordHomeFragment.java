package tingtel.payment.fragments.forgot_password;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordHomeFragment extends Fragment {

   NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_forgot_password_home, container, false);
        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_forgot_password_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));




        Button submitButton = view.findViewById(R.id.btn_get_otp);
//
        submitButton.setOnClickListener(v -> {
          navController.navigate(R.id.action_forgotPasswordHomeFragment_to_forgotPasswordOtpFragment);
        });



       return view;
    }
}
