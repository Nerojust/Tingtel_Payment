package tingtel.payment.fragments.dashboard;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import tingtel.payment.R;
import tingtel.payment.adapters.SpinnerAdapter;


public class TransferAirtimeReceiverInfoFragment extends Fragment {
    Spinner mSpinner;
    String[] spinnerTitles;
    String[] spinnerPopulation;
    int[] spinnerImages;
    String SenderSimNetwork;
    String ReceiverSimNetwork;
    int SimNo;
    String Amount;
    String SimSerial;
     Button btnPreview;
     NavController navController;
     EditText edPin;
     EditText edReceiverPhoneNumber;
     EditText edAmount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_transfer_airtime_receiver_info, container, false);

        SenderSimNetwork = getArguments().getString("simNetwork");
        SimNo = getArguments().getInt("simNo");
        Amount = getArguments().getString("amount");
        SimSerial = getArguments().getString("simSerial");

        mSpinner = (Spinner) view.findViewById(R.id.sp_network);
        btnPreview = (Button) view.findViewById(R.id.btn_preview);

        edPin = (EditText) view.findViewById(R.id.ed_pin);
        edReceiverPhoneNumber = (EditText) view.findViewById(R.id.ed_receiver_phone_number);
        edAmount = (EditText) view.findViewById(R.id.ed_amount);


        Fragment navhost = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(navhost);




        edAmount.setText("#" +Amount);



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



                switch (spinnerTitles[i]) {

                    case "Mtn":
                        ReceiverSimNetwork = "Mtn";
                        break;
                    case "Airtel":
                        ReceiverSimNetwork = "Airtel";
                        break;
                    case "9Mobile":
                        ReceiverSimNetwork = "9Mobile";
                        break;
                    case "Glo":
                        ReceiverSimNetwork = "Glo";
                        break;
                        default:
                        ReceiverSimNetwork = spinnerTitles[i];


                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("senderSimNetwork", SenderSimNetwork);
                bundle.putString("receiverSimNetwork", ReceiverSimNetwork);
                bundle.putString("simSerial", SimSerial);
                bundle.putInt("simNo", SimNo);
                bundle.putString("amount", Amount);
                bundle.putString("receiverPhoneNumber", edReceiverPhoneNumber.getText().toString());
                bundle.putString("pin", edPin.getText().toString());
                navController.navigate(R.id.action_transferAirtimeReceiverInfoFragment_to_transferAirtimePreviewFragment, bundle);


            }
        });


        return view;
    }

}
