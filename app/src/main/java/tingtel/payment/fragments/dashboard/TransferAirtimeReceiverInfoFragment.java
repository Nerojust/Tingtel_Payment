package tingtel.payment.fragments.dashboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.adapters.SpinnerAdapter;


public class TransferAirtimeReceiverInfoFragment extends Fragment {
    String[] spinnerPopulation;
    private String[] spinnerTitles;
    private int[] spinnerImages;
    private String SenderSimNetwork;
    private String ReceiverSimNetwork;
    private int SimNo;
    private String Amount;
    private String SimSerial;
    private Button btnPreview;
    private NavController navController;
    private EditText edPin;
    private EditText edReceiverPhoneNumber;
    private EditText edAmount;
    private SpinnerAdapter mCustomAdapter;
    private Spinner mSpinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_airtime_receiver_info, container, false);

        getExtrasFromIntent();
        initViews(view);
        initSpinner();
        initListeners();

        return view;
    }

    private void initListeners() {
        //todo: uncomment later
      /*  btnPreview.setOnClickListener(v -> {
            if (isValidFields()) {
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
        });*/
        btnPreview.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("senderSimNetwork", SenderSimNetwork);
            bundle.putString("receiverSimNetwork", ReceiverSimNetwork);
            bundle.putString("simSerial", SimSerial);
            bundle.putInt("simNo", SimNo);
            bundle.putString("amount", Amount);
            bundle.putString("receiverPhoneNumber", edReceiverPhoneNumber.getText().toString());
            bundle.putString("pin", edPin.getText().toString());
            navController.navigate(R.id.action_transferAirtimeReceiverInfoFragment_to_transferAirtimePreviewFragment, bundle);

        });
    }

    private void getExtrasFromIntent() {
        SenderSimNetwork = Objects.requireNonNull(getArguments()).getString("simNetwork");
        SimNo = getArguments().getInt("simNo");
        Amount = getArguments().getString("amount");
        SimSerial = getArguments().getString("simSerial");
    }

    private Spinner initViews(View view) {
        mSpinner = view.findViewById(R.id.sp_network);
        btnPreview = view.findViewById(R.id.btn_preview);

        edPin = view.findViewById(R.id.pinEditext);
        edReceiverPhoneNumber = view.findViewById(R.id.receivers_phone_number_edittext);
        edAmount = view.findViewById(R.id.ed_amount);

        Fragment navhost = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));

        edAmount.setText("#" + Amount);
        return mSpinner;
    }

    private void initSpinner() {
        spinnerTitles = new String[]{"Mtn", "Airtel", "9Mobile", "Glo"};
        spinnerImages = new int[]{R.drawable.mtn_logo, R.drawable.airtel_logo, R.drawable.nmobile_logo, R.drawable.glo_logo};

        mCustomAdapter = new SpinnerAdapter(Objects.requireNonNull(getActivity()), spinnerTitles, spinnerImages);
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
    }

    private boolean isValidFields() {
        if (edReceiverPhoneNumber.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter the receivers number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edPin.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Enter the receivers number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edReceiverPhoneNumber.getText().toString().length() < 11) {
            Toast.makeText(getContext(), "Phone number is too short", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edPin.getText().toString().length() < 4) {
            Toast.makeText(getContext(), "Pin is too short", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
