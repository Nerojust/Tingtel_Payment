package tingtel.payment.fragments.transfer_airtime;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save_beneficiary_sheet, container, false);

        String ReceiverPhoneNumber = getArguments().getString("ReceiverPhoneNumber");
        String Receivernetwork = getArguments().getString("ReceiverNetwork");



        return view;

    }



    private void saveBeneficiary() {


        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
/*
  in the background, save the data to roomdb using the balance model
 */
                Date currentDate = Calendar.getInstance().getTime();
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(getContext());

                //creating a task
                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setName();
                beneficiary.setPhoneNumber(ReceiverPhoneNumber);
                beneficiary.setNetwork(ReceiverSimNetwork);


                //adding to database
                appdatabase.beneficiaryDao().insert(beneficiary);

                return null;
            }
        }



    }

}
