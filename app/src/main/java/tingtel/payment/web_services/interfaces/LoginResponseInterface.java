package tingtel.payment.web_services.interfaces;


import retrofit2.Response;

public interface LoginResponseInterface {
    void onSuccess(Response loginResponses);

    void onError(String error);

    void onErrorCode(int errorCode);

}

