package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.validate_user.ValidateUserResponse;

public interface ValidateUserInterface {
    void onSuccess(ValidateUserResponse validateUserResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

