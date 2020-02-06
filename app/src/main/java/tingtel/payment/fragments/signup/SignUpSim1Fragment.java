package tingtel.payment.fragments.signup;


import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;




import java.util.ArrayList;

import tingtel.payment.R;
import tingtel.payment.adapters.SpinnerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpSim1Fragment extends Fragment {

Spinner  mSpinner;
    String[] spinnerTitles;
    String[] spinnerPopulation;
    int[] spinnerImages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_sim1, container, false);


        mSpinner = (Spinner) view.findViewById(R.id.sp_network);
        spinnerTitles = new String[]{"Mtn", "Airtel", "9Mobile", "Glo"};
       spinnerImages = new int[]{R.drawable.tingtel_logo
                , R.drawable.tingtellogo
              };

        SpinnerAdapter mCustomAdapter = new SpinnerAdapter (getActivity(), spinnerTitles, spinnerImages, spinnerPopulation);
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


}
