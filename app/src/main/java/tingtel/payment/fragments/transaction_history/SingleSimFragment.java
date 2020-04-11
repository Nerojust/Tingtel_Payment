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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import java.util.Objects;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.adapters.SingleHistoryAdapter;
import tingtel.payment.models.transaction_history.TransactionHistoryResponse;
import tingtel.payment.models.transaction_history.TransactionHistorySendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.TransactionHistoryInterface;

public class SingleSimFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayout noRecordFoundLayout;
    private AlertDialog alertDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View dialogView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_single_sim, container, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        ViewGroup viewGroup = Objects.requireNonNull(getActivity()).findViewById(android.R.id.content);
        dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_retry, viewGroup, false);
        builder.setView(dialogView);
        alertDialog = builder.create();


        initViews(view);
        if (AppUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            getHistory();
        } else {
            AppUtils.showSnackBar("No network available", noRecordFoundLayout);
        }


        return view;
    }

    private void initViews(View view) {
        noRecordFoundLayout = view.findViewById(R.id.no_result_found_layout_single);
        recyclerView = view.findViewById(R.id.rv_history_single);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout_single);

        swipeRefreshLayout.setOnRefreshListener(this::getHistory);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getHistory() {
        AppUtils.initLoadingDialog(getContext());

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
                    if (transactionHistoryResponse.getResults().get(0).getTransactionHistory().size() == 0) {
                        noRecordFoundLayout.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    } else {
                        SingleHistoryAdapter adapter = new SingleHistoryAdapter(getContext(), transactionHistoryResponse);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        noRecordFoundLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);

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
                getHistory();
            } else {
                AppUtils.showSnackBar("No network available", noRecordFoundLayout);
            }

            alertDialog.dismiss();
        });
        alertDialog.show();
    }
}
