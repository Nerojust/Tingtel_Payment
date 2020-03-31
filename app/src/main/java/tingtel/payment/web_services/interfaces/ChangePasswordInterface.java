package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.change_Password.ChangePasswordResponse;

public interface ChangePasswordInterface {
    void onSuccess(ChangePasswordResponse changePasswordResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

