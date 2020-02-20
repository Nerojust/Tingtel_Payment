package tingtel.payment.fragments.signup;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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
public class SignUpSim2OtpFragment extends Fragment {


    private Button btnConfirmOtp;
    private NavController navController;
    private String Sim2Network;
    private TextView resendOTP;
    private String Sim2Serial;
    private String Sim2PhoneNumber;
    private SessionManager sessionManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_sim2_otp, container, false);

        initViews(view);
        initListeners(view);
        getCarrierOfSim(getContext(), getActivity());
        getExtrasFromIntent();

        return view;
    }

    private void initListeners(View view) {
        btnConfirmOtp.setOnClickListener(v -> {
//todo:validate
            saveSimDetails();

            if (sessionManager.getIsRegistered()) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                getActivity().finish();
            } else {

                navController.navigate(R.id.action_signUpSim2OtpFragment_to_setPasswordFragment, null);
            }
        });
    }

    private void saveSimDetails() {

        //serial and network name gotten were already set by user so lets set the phone number & network name (ported numbers)
        sessionManager.setSimPhoneNumber1(Sim2PhoneNumber);
        sessionManager.setNetworkName1(Sim2Network);

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Date queryDate = Calendar.getInstance().getTime();
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getContext()));

                //creating a task
                SimCards simCards = new SimCards();

                simCards.setSimNetwork(Sim2Network);
                simCards.setSimSerial(Sim2Serial);
                simCards.setPhoneNumber(Sim2PhoneNumber);

                //adding to database
                appdatabase.simCardsDao().insert(simCards);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //Toast.makeText(getContext(), "Sim 2 Details Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }


    private void initViews(View view) {
        resendOTP = view.findViewById(R.id.resendOTPTextview);
        btnConfirmOtp = view.findViewById(R.id.btn_confirm_otp);

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_signup_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));
        sessionManager = AppUtils.getSessionManagerInstance();
    }

    private void getExtrasFromIntent() {
        Sim2Network = Objects.requireNonNull(getArguments()).getString("Sim2Network");
        Sim2Serial = getArguments().getString("Sim2Serial");
        Sim2PhoneNumber = getArguments().getString("Sim2PhoneNumber");
    }
}
