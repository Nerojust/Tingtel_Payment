package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.add_sim.AddSimResponse;

public interface AddSimInterface {
    void onSuccess(AddSimResponse addSimResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

