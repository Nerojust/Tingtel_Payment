package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.delete_sim.DeleteSimResponse;

public interface DeleteSimInterface {
    void onSuccess(DeleteSimResponse deleteSimResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

