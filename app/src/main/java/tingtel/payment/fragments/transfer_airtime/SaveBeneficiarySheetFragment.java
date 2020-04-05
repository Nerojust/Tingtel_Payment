package tingtel.payment.fragments.transfer_airtime;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.Beneficiary;
import tingtel.payment.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveBeneficiarySheetFragment extends BottomSheetDialogFragment {

    private EditText edName;
    private EditText edPhoneNumber;
    private EditText edNetworkName;
    private Button btnSave;
    private String ReceiverPhoneNumber;
    private String Receivernetwork;
    private String ReceiverName;
    private Activity activity;

    SaveBeneficiarySheetFragment(Activity activity) {
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save_beneficiary_sheet, container, false);

        if (getArguments() != null) {
            ReceiverPhoneNumber = getArguments().getString("ReceiverPhoneNumber");
            Receivernetwork = getArguments().getString("ReceiverNetwork");
        }

        initViews(view);
        initValues();
        initListeners();

        return view;
    }

    private void initListeners() {
        btnSave.setOnClickListener(v -> {
            if (!edName.getText().toString().isEmpty()) {
                if (AppUtils.isValidFieldsNumbersAndLetters(edName.getText().toString().trim())) {
                    saveBeneficiary();
                } else {
                    Toast.makeText(activity, "Invalid character/s detected", Toast.LENGTH_SHORT).show();
                }
            } else {
                AppUtils.showSnackBar("Fill in a name", edName);
                edName.requestFocus();
            }
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

        @SuppressLint("StaticFieldLeak")
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                // in the background, save the data to roomdb using the balance model
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getContext()));

                //creating a task
                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setName(ReceiverName);
                beneficiary.setPhoneNumber(ReceiverPhoneNumber);
                beneficiary.setNetwork(Receivernetwork);

                //adding to database
                appdatabase.beneficiaryDao().insert(beneficiary);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(activity, "Beneficiary saved", Toast.LENGTH_SHORT).show();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();

        dismiss();
    }
}
