package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.forgot_password.ForgotPasswordResponse;

public interface ForgotPasswordInterface {
    void onSuccess(ForgotPasswordResponse forgotPasswordResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

