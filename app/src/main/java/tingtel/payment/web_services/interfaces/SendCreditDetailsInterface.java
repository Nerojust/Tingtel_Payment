package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.send_credit.SendCreditDetailsResponse;

public interface SendCreditDetailsInterface {
    void onSuccess(SendCreditDetailsResponse changeEmailResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

