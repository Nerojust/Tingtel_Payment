package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.delete_account.DeleteAccountResponse;

public interface DeleteAccountInterface {
    void onSuccess(DeleteAccountResponse deleteAccountResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

