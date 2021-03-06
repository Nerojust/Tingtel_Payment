package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.login.CustomerLoginResponse;

public interface LoginResponseInterface {
    void onSuccess(CustomerLoginResponse loginResponses);

    void onError(String error);

    void onErrorCode(int errorCode);

}

