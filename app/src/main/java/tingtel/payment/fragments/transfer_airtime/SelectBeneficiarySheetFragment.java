package tingtel.payment.fragments.transfer_airtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.adapters.BeneficiaryAdapter;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.Beneficiary;


public class SelectBeneficiarySheetFragment extends BottomSheetDialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_beneficiary_sheet, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.rv_beneficiary);
        TextView noResult = view.findViewById(R.id.noResultFoundTextview);
        AppDatabase appDatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(getActivity()));

        List<Beneficiary> beneficiaryList = appDatabase.beneficiaryDao().getAllItems();
        BeneficiaryAdapter adapter = new BeneficiaryAdapter(getContext(), beneficiaryList, getActivity());

        if (beneficiaryList.size() == 0) {
           noResult.setVisibility(View.VISIBLE);
           noResult.setText("No beneficiary found");
        } else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            noResult.setVisibility(View.GONE);
            noResult.setText("");
        }

        return view;
    }


}
