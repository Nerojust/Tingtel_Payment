package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.otp.SendOTPresponse;

public interface SendOTPinterface {
    void onSuccess(SendOTPresponse sendOTPresponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

