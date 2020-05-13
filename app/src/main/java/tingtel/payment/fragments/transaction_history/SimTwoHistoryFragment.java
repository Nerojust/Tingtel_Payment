package tingtel.payment.fragments.transaction_history;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.history.main.PageViewModel;
import tingtel.payment.adapters.SimTwoHistoryAdapter;
import tingtel.payment.models.transaction_history.TransactionHistoryResponse;
import tingtel.payment.models.transaction_history.TransactionHistorySendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.TransactionHistoryInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimTwoHistoryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView recyclerView;
    private LinearLayout noRecordFoundLayout;
    private AlertDialog alertDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View dialogView;

    public SimTwoHistoryFragment() {
        // Required empty public constructor
    }

    public static SimTwoHistoryFragment newInstance(int index) {
        SimTwoHistoryFragment fragment = new SimTwoHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PageViewModel pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_sim_two_history, container, false);

        initViews(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        ViewGroup viewGroup = requireActivity().findViewById(android.R.id.content);
        dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_retry, viewGroup, false);
        builder.setView(dialogView);
        alertDialog = builder.create();


        if (AppUtils.isNetworkAvailable(requireActivity())) {
            getAllHistoryForSimTwo();
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.no_network_available), Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        return view;
    }

    private void initViews(View view) {
        noRecordFoundLayout = view.findViewById(R.id.no_result_found_layout);
        recyclerView = view.findViewById(R.id.rv_history_2);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this::getAllHistoryForSimTwo);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getAllHistoryForSimTwo() {
        TransactionHistorySendObject transactionHistorySendObject = new TransactionHistorySendObject();
        transactionHistorySendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));
        transactionHistorySendObject.setUserPhone(AppUtils.checkPhoneNumberAndRestructure(AppUtils.getSessionManagerInstance().getNumberFromLogin()));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(transactionHistorySendObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.getTransactionHistory(transactionHistorySendObject, new TransactionHistoryInterface() {
            @Override
            public void onSuccess(TransactionHistoryResponse transactionHistoryResponse) {
                if (transactionHistoryResponse != null) {
                    if (transactionHistoryResponse.getResults().size() == 0) {
                        noRecordFoundLayout.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    } else {
                        noRecordFoundLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);

                        SessionManager sessionManager = AppUtils.getSessionManagerInstance();
                        boolean found = false;

                        for (int i = 0; i < transactionHistoryResponse.getResults().size(); i++) {
                            Log.e("TingtelApp", "Server no is: " + transactionHistoryResponse.getResults().get(i).getPhoneNumber() + "Sim1No is " + sessionManager.getSimTwoPhoneNumber());

                            if (transactionHistoryResponse.getResults().get(i).getPhoneNumber() != null) {
                                if (transactionHistoryResponse.getResults().get(i).getPhoneNumber().equalsIgnoreCase(sessionManager.getSimTwoPhoneNumber())) {
                                    if (transactionHistoryResponse.getResults().get(i).getTransactionHistory().size() == 0) {
                                        swipeRefreshLayout.setVisibility(View.GONE);
                                        AppUtils.showDialog("No Record Found", getActivity());
                                        break;
                                    } else {
                                        found = true;
                                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                                        SimTwoHistoryAdapter historyAdapter = new SimTwoHistoryAdapter(getContext(), transactionHistoryResponse.getResults().get(i).getTransactionHistory());
                                        recyclerView.setAdapter(historyAdapter);
                                        historyAdapter.notifyDataSetChanged();
                                        if (swipeRefreshLayout.isRefreshing()) {
                                            swipeRefreshLayout.setRefreshing(false);
                                        }
                                        break;
                                    }

                                }
                            }

                        }
                        if (!found) {
                            noRecordFoundLayout.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }
                    }
                } else {
                    AppUtils.showSnackBar("Server Error", getView());
                }
            }

            @Override
            public void onError(String error) {
                displayDialog(error);
                noRecordFoundLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                //AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                AppUtils.showDialog(String.valueOf(errorCode), getActivity());
                //AppUtils.dismissLoadingDialog();
                noRecordFoundLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        });
    }

    private void displayDialog(String message) {
        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        TextView retry = dialogView.findViewById(R.id.btn_ok);
        TextView cancel = dialogView.findViewById(R.id.btn_no);

        tvMessage.setText(message);

        retry.setOnClickListener(v -> {
            if (AppUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                getAllHistoryForSimTwo();
            } else {
                AppUtils.showSnackBar(getResources().getString(R.string.no_network_available), noRecordFoundLayout);
            }
            alertDialog.dismiss();
        });
        cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
            Objects.requireNonNull(getActivity()).finish();
        });
        alertDialog.show();
    }
}
