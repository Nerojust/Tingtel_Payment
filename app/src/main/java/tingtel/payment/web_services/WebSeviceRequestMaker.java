package tingtel.payment.web_services;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import tingtel.payment.models.Change_Email.ChangeEmailResponse;
import tingtel.payment.models.Change_Email.ChangeEmailSendObject;
import tingtel.payment.models.Change_Password.ChangePasswordResponse;
import tingtel.payment.models.Change_Password.ChangePasswordSendObject;
import tingtel.payment.models.CustomerInfo.CustomerInfoResponse;
import tingtel.payment.models.CustomerInfo.CustomerInfoSendObject;
import tingtel.payment.models.Login.CustomerLoginResponse;
import tingtel.payment.models.Login.CustomerLoginSendObject;
import tingtel.payment.models.Registration.CustomerRegistrationResponse;
import tingtel.payment.models.Registration.CustomerRegistrationSendObject;
import tingtel.payment.models.Report_Issue.ReportIssueResponse;
import tingtel.payment.models.Report_Issue.ReportIssueSendObject;
import tingtel.payment.utils.Constants;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.web_services.interfaces.ChangeEmailInterface;
import tingtel.payment.web_services.interfaces.ChangePasswordInterface;
import tingtel.payment.web_services.interfaces.CreateNewUserInterface;
import tingtel.payment.web_services.interfaces.GetUserProfileInterface;
import tingtel.payment.web_services.interfaces.LoginResponseInterface;
import tingtel.payment.web_services.interfaces.ReportIssueInterface;

public class WebSeviceRequestMaker {


    private final String TAG = "WebserviceRequestMaker";
    private final Retrofit retrofit = MyApplication.getInstance().getRetrofit();
    private final PostServiceInterface postInterfaceService = retrofit.create(PostServiceInterface.class);

    public void createANewUser(CustomerRegistrationSendObject customerRegistrationSendObject, CreateNewUserInterface createNewUserInterface) {
        Call<CustomerRegistrationResponse> call = postInterfaceService.createNewUser(customerRegistrationSendObject);
        call.enqueue(new Callback<CustomerRegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<CustomerRegistrationResponse> call, @NonNull Response<CustomerRegistrationResponse> response) {
                if (response.isSuccessful()) {
                    CustomerRegistrationResponse createNewuserResponse = response.body();

                    if (createNewuserResponse != null) {
                        if (createNewuserResponse.getCode().equals(Constants.SUCCESS)) {
                            createNewUserInterface.onSuccess(createNewuserResponse);
                        } else {
                            createNewUserInterface.onError(createNewuserResponse.getDescription());
                        }
                    }
                } else {
                    createNewUserInterface.onError(response.message());
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerRegistrationResponse> call, @NonNull Throwable t) {
                if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                    createNewUserInterface.onError("Network Error, please try again");
                } else {
                    createNewUserInterface.onError(t.getMessage());
                }
                String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                Log.e("Login error", error);
            }
        });
    }

    public void loginInUser(CustomerLoginSendObject customerLoginSendObject, LoginResponseInterface loginResponseInterface) {
        Call<CustomerLoginResponse> call = postInterfaceService.loginUser(customerLoginSendObject);
        call.enqueue(new Callback<CustomerLoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<CustomerLoginResponse> call, @NonNull Response<CustomerLoginResponse> response) {
                if (response.isSuccessful()) {
                    CustomerLoginResponse createNewuserResponse = response.body();

                    if (createNewuserResponse != null) {
                        if (createNewuserResponse.getCode().equals(Constants.SUCCESS)) {
                            loginResponseInterface.onSuccess(createNewuserResponse);
                        } else {
                            if (createNewuserResponse.getDescription().contains("failed to connect")) {
                                loginResponseInterface.onError("Network error");
                            } else {
                                loginResponseInterface.onError(createNewuserResponse.getDescription());
                            }
                        }
                    }
                } else {
                    loginResponseInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerLoginResponse> call, @NonNull Throwable t) {
                if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                    loginResponseInterface.onError("Network Error, please try again");
                } else {
                    loginResponseInterface.onError(t.getMessage());
                }
                String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                Log.e("Login error", error);
            }
        });
    }

    public void getCustomerDetails(CustomerInfoSendObject customerInfoSendObject, GetUserProfileInterface getUserProfileInterface) {
        Call<CustomerInfoResponse> call = postInterfaceService.getCustomerInfo(customerInfoSendObject);
        call.enqueue(new Callback<CustomerInfoResponse>() {
            @Override
            public void onResponse(@NonNull Call<CustomerInfoResponse> call, @NonNull Response<CustomerInfoResponse> response) {
                if (response.isSuccessful()) {
                    CustomerInfoResponse customerInfoResponse = response.body();

                    if (customerInfoResponse != null) {
                        getUserProfileInterface.onSuccess(customerInfoResponse);
                    } else {
                        getUserProfileInterface.onError("No record found");

                    }
                } else {
                    getUserProfileInterface.onError(response.message());
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerInfoResponse> call, @NonNull Throwable t) {
                getUserProfileInterface.onError(t.getMessage());
                String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                Log.e("Login error", error);
            }
        });
    }

    public void changeEmailAddress(ChangeEmailSendObject changeEmailSendObject, ChangeEmailInterface changeEmailInterface) {
        Call<ChangeEmailResponse> call = postInterfaceService.changeEmailAddress(changeEmailSendObject);
        call.enqueue(new Callback<ChangeEmailResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChangeEmailResponse> call, @NonNull Response<ChangeEmailResponse> response) {
                if (response.isSuccessful()) {
                    ChangeEmailResponse changeEmailResponse = response.body();

                    if (changeEmailResponse != null) {
                        if (changeEmailResponse.getCode().equals(Constants.SUCCESS)) {
                            changeEmailInterface.onSuccess(changeEmailResponse);
                        } else {
                            if (changeEmailResponse.getDescription().contains("failed to connect")) {
                                changeEmailInterface.onError("Network Error, please try again");
                            } else {
                                changeEmailInterface.onError(changeEmailResponse.getDescription());
                            }
                        }
                    }
                } else {
                    changeEmailInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChangeEmailResponse> call, @NonNull Throwable t) {
                if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                    changeEmailInterface.onError("Network Error, please try again");
                } else {
                    changeEmailInterface.onError(t.getMessage());
                }

                String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                Log.e("Login error", error);
            }
        });
    }

    public void changePassword(ChangePasswordSendObject changePasswordSendObject, ChangePasswordInterface changePasswordInterface) {
        Call<ChangePasswordResponse> call = postInterfaceService.changePassword(changePasswordSendObject);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChangePasswordResponse> call, @NonNull Response<ChangePasswordResponse> response) {
                if (response.isSuccessful()) {
                    ChangePasswordResponse changePasswordResponse = response.body();

                    if (changePasswordResponse != null) {
                        if (changePasswordResponse.getCode().equals(Constants.SUCCESS)) {
                            changePasswordInterface.onSuccess(changePasswordResponse);
                        } else {
                            if (changePasswordResponse.getDescription().contains("failed to connect")) {
                                changePasswordInterface.onError("Network Error, please try again");
                            } else {
                                changePasswordInterface.onError(changePasswordResponse.getDescription());
                            }
                        }
                    }
                } else {
                    changePasswordInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChangePasswordResponse> call, @NonNull Throwable t) {
                if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                    changePasswordInterface.onError("Network Error, please try again");
                } else {
                    changePasswordInterface.onError(t.getMessage());
                }
                String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                Log.e("Login error", error);
            }
        });
    }

    public void reportAnIssue(ReportIssueSendObject reportIssueSendObject, ReportIssueInterface reportIssueInterface) {
        Call<ReportIssueResponse> call = postInterfaceService.reportIssue(reportIssueSendObject);
        call.enqueue(new Callback<ReportIssueResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReportIssueResponse> call, @NonNull Response<ReportIssueResponse> response) {
                if (response.isSuccessful()) {
                    ReportIssueResponse reportIssueResponse = response.body();

                    if (reportIssueResponse != null) {
                        if (reportIssueResponse.getCode().equals(Constants.SUCCESS)) {
                            reportIssueInterface.onSuccess(reportIssueResponse);
                        } else {
                            reportIssueInterface.onError(reportIssueResponse.getDescription());
                        }
                    }
                } else {
                    reportIssueInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReportIssueResponse> call, @NonNull Throwable t) {

                if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                    reportIssueInterface.onError("Network Error, please try again");
                } else {
                    reportIssueInterface.onError(t.getMessage());
                }
                String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                Log.e("Login error", error);
            }
        });
    }

}
