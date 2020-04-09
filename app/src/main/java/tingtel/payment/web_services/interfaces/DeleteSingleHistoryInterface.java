package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.delete_transaction.DeleteTransactionResponse;

public interface DeleteSingleHistoryInterface {
    void onSuccess(DeleteTransactionResponse deleteTransactionResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

