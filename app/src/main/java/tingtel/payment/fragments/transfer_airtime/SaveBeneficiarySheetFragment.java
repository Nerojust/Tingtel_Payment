package tingtel.payment.fragments.transfer_airtime;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Date;

import tingtel.payment.R;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.Beneficiary;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveBeneficiarySheetFragment extends BottomSheetDialogFragment {

    EditText edName;
    EditText edPhoneNumber;
    EditText edNetworkName;
    Button btnSave;
    String ReceiverPhoneNumber;
    String Receivernetwork;
    String ReceiverName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save_beneficiary_sheet, container, false);

        ReceiverPhoneNumber = getArguments().getString("ReceiverPhoneNumber");
        Receivernetwork = getArguments().getString("ReceiverNetwork");

        initViews(view);
        initValues();
        initListeners();


        return view;

    }

    private void initListeners() {
        btnSave.setOnClickListener(v -> {
         saveBeneficiary();
        });
    }

    private void initValues() {
        edPhoneNumber.setText(ReceiverPhoneNumber);
        edNetworkName.setText(Receivernetwork);
    }

    private void initViews(View view) {
        edName = view.findViewById(R.id.ed_name);
        edPhoneNumber = view.findViewById(R.id.ed_phone_number);
        edNetworkName = view.findViewById(R.id.ed_network_name);
        btnSave = view.findViewById(R.id.btn_save);
    }


    private void saveBeneficiary() {

        ReceiverName = edName.getText().toString();

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
/*
  in the background, save the data to roomdb using the balance model
 */

                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(getContext());

                //creating a task
                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setName(ReceiverName);
                beneficiary.setPhoneNumber(ReceiverPhoneNumber);
                beneficiary.setNetwork(Receivernetwork);

                //adding to database
                appdatabase.beneficiaryDao().insert(beneficiary);

                return null;
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
        dismiss();


    }

}
