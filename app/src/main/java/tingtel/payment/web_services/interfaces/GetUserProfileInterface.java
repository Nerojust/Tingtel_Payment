package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.customerInfo.CustomerInfoResponse;

public interface GetUserProfileInterface {
    void onSuccess(CustomerInfoResponse customerInfoResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

