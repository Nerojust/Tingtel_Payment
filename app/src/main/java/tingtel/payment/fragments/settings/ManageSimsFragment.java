package tingtel.payment.fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.R;
import tingtel.payment.adapters.SimCardsAdapter;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;


public class ManageSimsFragment extends Fragment {

    List<SimCards> simCards = new ArrayList<>();
    AppDatabase appDatabase;
    RecyclerView recyclerView;
   SimCardsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_sims, container, false);

        recyclerView = view.findViewById(R.id.rv_simCards);

        appDatabase = AppDatabase.getDatabaseInstance(getContext());

        simCards = appDatabase.simCardsDao().getAllItems();
        adapter = new SimCardsAdapter(getContext(), simCards, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
