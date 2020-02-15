package tingtel.payment.fragments.settings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.R;
import tingtel.payment.adapters.SettingsAdapter;


public class SettingsHome extends Fragment {

    List<String> settingsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SettingsAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings_home, container, false);

        recyclerView = view.findViewById(R.id.rv_settings);

        LoadRecyclerView();

        return view;
    }



    private void LoadRecyclerView() {

        mAdapter = new SettingsAdapter(getContext(), settingsList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        settingsList.clear();
        prepareeSettings();
    }

    private void prepareeSettings() {
        settingsList.add("Change Password");
        settingsList.add("Change Email Address");
        settingsList.add("Add New Sim");
        settingsList.add("Manage Sims");
        settingsList.add("Tutorial (How to Use)");
        settingsList.add("Report An Issue");
        settingsList.add("Privacy Policy");
        settingsList.add("Delete Account");
        settingsList.add("Share App");
        mAdapter.notifyDataSetChanged();
    }
}
