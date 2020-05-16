package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.credit_notification.CreditNotificationResponse;

public interface CreditNotificationInterface {
    void onSuccess(CreditNotificationResponse creditNotificationResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

