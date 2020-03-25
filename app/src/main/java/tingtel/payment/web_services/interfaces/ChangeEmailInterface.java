package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.Change_Email.ChangeEmailResponse;

public interface ChangeEmailInterface {
    void onSuccess(ChangeEmailResponse changeEmailResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

