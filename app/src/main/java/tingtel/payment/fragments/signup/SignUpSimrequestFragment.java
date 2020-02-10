package tingtel.payment.fragments.signup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.NetworkCarrierUtils.getCarrierOfSim;


public class SignUpSimrequestFragment extends Fragment {

    RelativeLayout btnRegister1Sim;
    RelativeLayout btnRegister2Sim;
    NavController navController;
    SessionManager sessionManager;
    String Sim1Serial, Sim2Serial;
    String Sim1Network, Sim2Network;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_simrequest, container, false);

        initViews(view);
        initListeners(view);
        getCarrierOfSim(getContext(), getActivity());
        getDataFromCarrier(view);


        return view;
    }

    private void initViews(View view) {
        btnRegister1Sim = view.findViewById(R.id.rel_register_1_sim);
        btnRegister2Sim = view.findViewById(R.id.rel_register_2_sim);

        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(navhost);

        sessionManager = AppUtils.getSessionManagerInstance();
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


    private void getDataFromCarrier(View view) {


        Sim1Network = sessionManager.getNetworkName();
        Sim2Network = sessionManager.getNetworkName1();

        Sim1Serial = sessionManager.getSimSerialICCID();
        Sim2Serial = sessionManager.getSimSerialICCID1();


        String NoOfSIm = sessionManager.getSimStatus();
        Log.e("getDefaultCarrier", "No of sim is " + NoOfSIm);

        switch (NoOfSIm) {
            case "NO SIM":
                Toast.makeText(getActivity(), "Please insert a sim card", Toast.LENGTH_SHORT).show();
                break;
            case "SIM1":

                btnRegister1Sim.setVisibility(View.VISIBLE);
                btnRegister2Sim.setVisibility(View.GONE);
                break;
            case "SIM1 SIM2":

                btnRegister1Sim.setVisibility(View.VISIBLE);
                btnRegister2Sim.setVisibility(View.VISIBLE);
                break;
        }
    }


}
