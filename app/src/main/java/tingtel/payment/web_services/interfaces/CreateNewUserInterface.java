package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.registration.CustomerRegistrationResponse;

public interface CreateNewUserInterface {
    void onSuccess(CustomerRegistrationResponse newUser);

    void onError(String error);

    void onErrorCode(int errorCode);

}

