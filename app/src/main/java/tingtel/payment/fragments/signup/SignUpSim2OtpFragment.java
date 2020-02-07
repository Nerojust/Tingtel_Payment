package tingtel.payment.fragments.signup;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tingtel.payment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpSim2OtpFragment extends Fragment {


    Button btnConfirmOtp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_sim2_otp, container, false);

        initViews(view);
        initListeners(view);


        return view;
    }

    private void initListeners(View view) {
        btnConfirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initViews(View view) {
        btnConfirmOtp = view.findViewById(R.id.btn_confirm_otp);
    }

}
