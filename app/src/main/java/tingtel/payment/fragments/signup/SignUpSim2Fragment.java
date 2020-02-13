package tingtel.payment.fragments.signup;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import tingtel.payment.R;
import tingtel.payment.adapters.SpinnerAdapter;

public class SignUpSim2Fragment extends Fragment {

    Spinner  mSpinner;
    String[] spinnerTitles;
    String[] spinnerPopulation;
    int[] spinnerImages;
    Button btnNext;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;

        view = inflater.inflate(R.layout.fragment_sign_up_sim2, container, false);

        initViews(view);
        initListeners(view);

        mSpinner = (Spinner) view.findViewById(R.id.sp_network);
        spinnerTitles = new String[]{"Mtn", "Airtel", "9Mobile", "Glo"};
        spinnerImages = new int[]{R.drawable.mtn_logo
                , R.drawable.airtel_logo, R.drawable.nmobile_logo, R.drawable.glo_logo
        };

        SpinnerAdapter mCustomAdapter = new SpinnerAdapter (getActivity(), spinnerTitles, spinnerImages);
        mSpinner.setAdapter(mCustomAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getActivity(), spinnerTitles[i], Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    private void initListeners(View view) {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signUpSim2Fragment_to_signUpSim2OtpFragment2, null);
            }
        });
    }

    private void initViews(View view) {
        btnNext = view.findViewById(R.id.btn_next);


        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(navhost);
    }

}
