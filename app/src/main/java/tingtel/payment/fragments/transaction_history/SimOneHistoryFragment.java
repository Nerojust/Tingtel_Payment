package tingtel.payment.fragments.transaction_history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.activities.history.main.PageViewModel;
import tingtel.payment.adapters.SimOneHistoryAdapter;
import tingtel.payment.models.transaction_history.TransactionHistoryResponse;
import tingtel.payment.models.transaction_history.TransactionHistorySendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.TransactionHistoryInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimOneHistoryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView recyclerView;
    private LinearLayout noRecordFoundLayout;
    private AlertDialog alertDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AlertDialog.Builder builder;
    private View dialogView;
    private PageViewModel pageViewModel;

    public SimOneHistoryFragment() {
        // Required empty public constructor
    }

    public static SimOneHistoryFragment newInstance(int index) {
        SimOneHistoryFragment fragment = new SimOneHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_sim_one_history, container, false);
        builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        ViewGroup viewGroup = Objects.requireNonNull(getActivity()).findViewById(android.R.id.content);
        dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_retry, viewGroup, false);
        builder.setView(dialogView);
        alertDialog = builder.create();


        initViews(view);
        if (AppUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            getAllHistory();
        } else {
            AppUtils.showSnackBar("No network available", noRecordFoundLayout);
        }


        return view;
    }

    private void initViews(View view) {
        noRecordFoundLayout = view.findViewById(R.id.no_result_found_layout);
        recyclerView = view.findViewById(R.id.rv_history_1);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setOnRefreshListener(this::getAllHistory);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getAllHistory() {
        AppUtils.initLoadingDialog(getContext());

        TransactionHistorySendObject transactionHistorySendObject = new TransactionHistorySendObject();
        transactionHistorySendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));
        transactionHistorySendObject.setUserPhone(AppUtils.checkPhoneNumberAndRestructure(AppUtils.getSessionManagerInstance().getSimPhoneNumber()));

        Gson gson = new Gson();
        String jsonObject = gson.toJson(transactionHistorySendObject);

        WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
        webSeviceRequestMaker.getTransactionHistory(transactionHistorySendObject, new TransactionHistoryInterface() {
            @Override
            public void onSuccess(TransactionHistoryResponse transactionHistoryResponse) {
                if (transactionHistoryResponse != null) {
                    if (transactionHistoryResponse.getPhone1Transactions().size() == 0) {
                        noRecordFoundLayout.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    } else {
                        noRecordFoundLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        SimOneHistoryAdapter historyAdapter = new SimOneHistoryAdapter(getActivity(), transactionHistoryResponse);
                        recyclerView.setAdapter(historyAdapter);
                        historyAdapter.notifyDataSetChanged();
                        recyclerView.setHasFixedSize(true);
                        recyclerView.smoothScrollToPosition(0);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                } else {
                    AppUtils.showDialog("Server Error", getActivity());
                }

                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onError(String error) {

                displayDialog(error);
                noRecordFoundLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                AppUtils.dismissLoadingDialog();
            }

            @Override
            public void onErrorCode(int errorCode) {
                //AppUtils.showDialog(String.valueOf(errorCode), getActivity());
                noRecordFoundLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                AppUtils.dismissLoadingDialog();
            }
        });
    }
//todo: check this
//    @Override
//    public void onDetach() {
//        boolean status = AppUtils.getSessionManagerInstance().getComingFromSuccess();
//        if (status) {
//            startActivity(new Intent(getContext(), MainActivity.class));
//            Objects.requireNonNull(getActivity()).finish();
//        }
//        super.onDetach();
//
//    }


    private void displayDialog(String message) {
        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        Button retry = dialogView.findViewById(R.id.btn_ok);

        tvMessage.setText(message);
        retry.setOnClickListener(v -> {
            if (AppUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
                getAllHistory();
            } else {
                AppUtils.showSnackBar("No network available", noRecordFoundLayout);
            }

            alertDialog.dismiss();
        });
        alertDialog.show();
    }
}
