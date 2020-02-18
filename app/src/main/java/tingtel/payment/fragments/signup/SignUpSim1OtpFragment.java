package tingtel.payment.fragments.signup;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import tingtel.payment.MainActivity;
import tingtel.payment.R;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

import static tingtel.payment.utils.NetworkCarrierUtils.getCarrierOfSim;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpSim1OtpFragment extends Fragment {


   Button btnConfirmOtp;
    NavController navController;
    String Sim1Network;
    String Sim1Serial;
    String Sim1PhoneNumber;
    SessionManager sessionManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_sim1_otp, container, false);

        initViews(view);
        initListeners(view);
        getCarrierOfSim(getContext(), getActivity());

        return view;
    }

    private void initListeners(View view) {

        btnConfirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveSimDetails();


                if (sessionManager.getIsRegistered()) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                } else {


                    String NoOfSIm = sessionManager.getSimStatus();

                    switch (NoOfSIm) {

                        case "SIM1":

                            navController.navigate(R.id.action_signUpSim1OtpFragment_to_setPasswordFragment, null);
                            break;
                        case "SIM1 SIM2":

                            navController.navigate(R.id.action_signUpSim1OtpFragment_to_SIgnUpSim1SuccessFragment, null);
                            break;
                    }
                }


            }
        });

    }

    private void saveSimDetails() {

        //serial and network name gotten were already set by user so lets set the phone number & network name (ported numbers)
        sessionManager.setSimPhoneNumber(Sim1PhoneNumber);
        sessionManager.setNetworkName(Sim1Network);

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Date queryDate = Calendar.getInstance().getTime();
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(getContext());

                //creating a task
                SimCards simCards = new SimCards();

                simCards.setSimNetwork(Sim1Network);
                simCards.setSimSerial(Sim1Serial);
                simCards.setPhoneNumber(Sim1PhoneNumber);

                //adding to database
                appdatabase.simCardsDao().insert(simCards);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getContext(), "Sim 1 Details Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    private void initViews(View view) {

        btnConfirmOtp = view.findViewById(R.id.btn_confirm_otp);

        Sim1Network = getArguments().getString("Sim1Network");
        Sim1Serial = getArguments().getString("Sim1Serial");
        Sim1PhoneNumber = getArguments().getString("Sim1PhoneNumber");

        sessionManager = AppUtils.getSessionManagerInstance();

        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(navhost);

    }

}
