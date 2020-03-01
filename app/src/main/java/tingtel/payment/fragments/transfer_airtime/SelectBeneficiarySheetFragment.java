package tingtel.payment.fragments.transfer_airtime;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.R;
import tingtel.payment.adapters.BeneficiaryAdapter;
import tingtel.payment.adapters.SimCardsAdapter;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.Beneficiary;
import tingtel.payment.models.SimCards;


public class SelectBeneficiarySheetFragment extends BottomSheetDialogFragment {


    List<Beneficiary> beneficiaryList = new ArrayList<>();
    AppDatabase appDatabase;
    RecyclerView recyclerView;
    BeneficiaryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_beneficiary_sheet, container, false);



        recyclerView = view.findViewById(R.id.rv_beneficiary);

        appDatabase = AppDatabase.getDatabaseInstance(getActivity());

        beneficiaryList = appDatabase.beneficiaryDao().getAllItems();
        adapter = new BeneficiaryAdapter(getContext(), beneficiaryList,getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return view;
    }


}
