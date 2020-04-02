package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.transaction_history.TransactionHistoryResponse;

public interface TransactionHistoryInterface {
    void onSuccess(TransactionHistoryResponse transactionHistoryResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

