package tingtel.payment.web_services;

import retrofit2.Retrofit;
import tingtel.payment.utils.MyApplication;

public class WebSeviceRequestMaker {


    private final String TAG = "WebserviceRequestMaker";
    private final Retrofit retrofit = MyApplication.getInstance().getRetrofit();
    private final PostServiceIinteface postInterfaceService = retrofit.create(PostServiceIinteface.class);

    /*public void createUser(CreateUserSendObject createUserSendObject, CreateNewUserInterface createNewUserInterface) {
        Call<CreateUserResponse> call = postInterfaceService.createAnewUser(createUserSendObject);
        call.enqueue(new Callback<CreateUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateUserResponse> call, @NonNull Response<CreateUserResponse> response) {
                if (response.isSuccessful()) {
                    CreateUserResponse loginResponse = response.body();

                    assert loginResponse != null;
                    if (loginResponse.getVerified() == 0) {
                        createNewUserInterface.onSuccess(response.body());
                    } else {
                        createNewUserInterface.onError(response.message());
                        Log.d(TAG, loginResponse.getUsername());
                    }
                } else {
                    createNewUserInterface.onError(response.message());
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateUserResponse> call, @NonNull Throwable t) {
                createNewUserInterface.onError(t.getMessage());
                String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                Log.e("Login error", error);
            }
        });
    }*/
}
