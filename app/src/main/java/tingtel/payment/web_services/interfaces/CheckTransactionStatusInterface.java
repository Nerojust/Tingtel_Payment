package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.transaction_status.CheckTransactionStatusResponse;

public interface CheckTransactionStatusInterface {
    void onSuccess(CheckTransactionStatusResponse checkTransactionStatusResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

