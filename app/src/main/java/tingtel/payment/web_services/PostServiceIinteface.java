package tingtel.payment.web_services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tingtel.payment.models.CustomerInfo.CustomerInfoResponse;
import tingtel.payment.models.CustomerInfo.CustomerInfoSendObject;
import tingtel.payment.models.Login.CustomerLoginResponse;
import tingtel.payment.models.Login.CustomerLoginSendObject;
import tingtel.payment.models.Registration.CustomerRegistrationResponse;
import tingtel.payment.models.Registration.CustomerRegistrationSendObject;

public interface PostServiceIinteface {

    @POST("register")
    Call<CustomerRegistrationResponse> createNewUser(@Body CustomerRegistrationSendObject customerRegistrationSendObject);

    @POST("login")
    Call<CustomerLoginResponse> loginUser(@Body CustomerLoginSendObject customerLoginSendObject);

    @POST("user_info")
    Call<CustomerInfoResponse> getCustomerInfo(@Body CustomerInfoSendObject customerInfoSendObject);
}
