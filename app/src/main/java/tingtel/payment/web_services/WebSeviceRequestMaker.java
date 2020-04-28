package tingtel.payment.web_services;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import tingtel.payment.models.add_sim.AddSimResponse;
import tingtel.payment.models.add_sim.AddSimSendObject;
import tingtel.payment.models.change_Email.ChangeEmailResponse;
import tingtel.payment.models.change_Email.ChangeEmailSendObject;
import tingtel.payment.models.change_Password.ChangePasswordResponse;
import tingtel.payment.models.change_Password.ChangePasswordSendObject;
import tingtel.payment.models.customerInfo.CustomerInfoResponse;
import tingtel.payment.models.customerInfo.CustomerInfoSendObject;
import tingtel.payment.models.delete_account.DeleteAccountResponse;
import tingtel.payment.models.delete_account.DeleteAccountSendObject;
import tingtel.payment.models.delete_sim.DeleteSimResponse;
import tingtel.payment.models.delete_sim.DeleteSimSendObject;
import tingtel.payment.models.delete_transaction.DeleteTransactionResponse;
import tingtel.payment.models.delete_transaction.DeleteTransactionSendObject;
import tingtel.payment.models.forgot_password.ForgotPasswordResponse;
import tingtel.payment.models.forgot_password.ForgotPasswordSendObject;
import tingtel.payment.models.login.CustomerLoginResponse;
import tingtel.payment.models.login.CustomerLoginSendObject;
import tingtel.payment.models.otp.SendOTPresponse;
import tingtel.payment.models.otp.SendOTPsendObject;
import tingtel.payment.models.registration.CustomerRegistrationResponse;
import tingtel.payment.models.registration.CustomerRegistrationSendObject;
import tingtel.payment.models.report_Issue.ReportIssueResponse;
import tingtel.payment.models.report_Issue.ReportIssueSendObject;
import tingtel.payment.models.send_credit.SendCreditDetailsResponse;
import tingtel.payment.models.send_credit.SendCreditDetailsSendObject;
import tingtel.payment.models.transaction_history.TransactionHistoryResponse;
import tingtel.payment.models.transaction_history.TransactionHistorySendObject;
import tingtel.payment.models.transaction_status.CheckTransactionStatusResponse;
import tingtel.payment.models.transaction_status.CheckTransactionStatusSendObject;
import tingtel.payment.utils.Constants;
import tingtel.payment.utils.MyApplication;
import tingtel.payment.web_services.interfaces.AddSimInterface;
import tingtel.payment.web_services.interfaces.ChangeEmailInterface;
import tingtel.payment.web_services.interfaces.ChangePasswordInterface;
import tingtel.payment.web_services.interfaces.CheckTransactionStatusInterface;
import tingtel.payment.web_services.interfaces.CreateNewUserInterface;
import tingtel.payment.web_services.interfaces.DeleteAccountInterface;
import tingtel.payment.web_services.interfaces.DeleteSimInterface;
import tingtel.payment.web_services.interfaces.DeleteSingleHistoryInterface;
import tingtel.payment.web_services.interfaces.ForgotPasswordInterface;
import tingtel.payment.web_services.interfaces.GetUserProfileInterface;
import tingtel.payment.web_services.interfaces.LoginResponseInterface;
import tingtel.payment.web_services.interfaces.ReportIssueInterface;
import tingtel.payment.web_services.interfaces.SendCreditDetailsInterface;
import tingtel.payment.web_services.interfaces.SendOTPinterface;
import tingtel.payment.web_services.interfaces.TransactionHistoryInterface;

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
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        createNewUserInterface.onError("Network Error, please try again");
                    } else {
                        createNewUserInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    createNewUserInterface.onError("Network error");
                }
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
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        loginResponseInterface.onError("Network Error, please try again");
                    } else {
                        loginResponseInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    loginResponseInterface.onError("Network error");
                }
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
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        changeEmailInterface.onError("Network Error, please try again");
                    } else {
                        changeEmailInterface.onError(t.getMessage());
                    }

                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    changeEmailInterface.onError("Network error");
                }
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
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        changePasswordInterface.onError("Network Error, please try again");
                    } else {
                        changePasswordInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    changePasswordInterface.onError("Network error");
                }
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
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        reportIssueInterface.onError("Network Error, please try again");
                    } else {
                        reportIssueInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    reportIssueInterface.onError("Network error");
                }
            }
        });
    }

    public void sendCreditDetailsToServer(SendCreditDetailsSendObject transactionHistorySendObject, SendCreditDetailsInterface sendCreditDetailsInterface) {
        Call<SendCreditDetailsResponse> call = postInterfaceService.sendCreditInfoToServer(transactionHistorySendObject);
        call.enqueue(new Callback<SendCreditDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SendCreditDetailsResponse> call, @NonNull Response<SendCreditDetailsResponse> response) {
                if (response.isSuccessful()) {
                    SendCreditDetailsResponse sendCreditDetailsResponse = response.body();

                    if (sendCreditDetailsResponse != null) {
                        if (sendCreditDetailsResponse.getCode().equals(Constants.SUCCESS)) {
                            sendCreditDetailsInterface.onSuccess(sendCreditDetailsResponse);
                        } else {
                            sendCreditDetailsInterface.onError(sendCreditDetailsResponse.getDescription());
                        }
                    }
                } else {
                    sendCreditDetailsInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SendCreditDetailsResponse> call, @NonNull Throwable t) {
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        sendCreditDetailsInterface.onError("Network Error, please try again");
                    } else {
                        sendCreditDetailsInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    sendCreditDetailsInterface.onError("Network error");
                }
            }
        });
    }


    public void sendOTPtoCustomer(SendOTPsendObject sendOTPsendObject, SendOTPinterface sendOTPinterface) {
        Call<SendOTPresponse> call = postInterfaceService.sendOTP(sendOTPsendObject);
        call.enqueue(new Callback<SendOTPresponse>() {
            @Override
            public void onResponse(@NonNull Call<SendOTPresponse> call, @NonNull Response<SendOTPresponse> response) {
                if (response.isSuccessful()) {
                    SendOTPresponse sendOTPresponse = response.body();

                    if (sendOTPresponse != null) {
                        if (sendOTPresponse.getCode().equals(Constants.SUCCESS)) {
                            sendOTPinterface.onSuccess(sendOTPresponse);
                        } else {
                            sendOTPinterface.onError(sendOTPresponse.getDescription());
                        }
                    }
                } else {
                    sendOTPinterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SendOTPresponse> call, @NonNull Throwable t) {
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        sendOTPinterface.onError("Network Error, please try again");
                    } else {
                        sendOTPinterface.onError(t.getMessage());
                    }

                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    sendOTPinterface.onError("Network error");
                }
            }
        });
    }

    public void checkTransactionStatus(CheckTransactionStatusSendObject checkTransactionStatusSendObject,
                                       CheckTransactionStatusInterface checkTransactionStatusInterface) {
        Call<CheckTransactionStatusResponse> call = postInterfaceService.checkStatusOfTransaction(checkTransactionStatusSendObject);
        call.enqueue(new Callback<CheckTransactionStatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<CheckTransactionStatusResponse> call, @NonNull Response<CheckTransactionStatusResponse> response) {
                if (response.isSuccessful()) {
                    CheckTransactionStatusResponse checkTransactionStatusResponse = response.body();

                    if (checkTransactionStatusResponse != null) {
                        checkTransactionStatusInterface.onSuccess(checkTransactionStatusResponse);
                    } else {
                        checkTransactionStatusInterface.onError("No record found");
                    }
                } else {
                    checkTransactionStatusInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckTransactionStatusResponse> call, @NonNull Throwable t) {
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        checkTransactionStatusInterface.onError("Network Error, please try again");
                    } else {
                        checkTransactionStatusInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    checkTransactionStatusInterface.onError("Network error");
                }
            }
        });
    }

    public void getTransactionHistory(TransactionHistorySendObject transactionHistorySendObject, TransactionHistoryInterface transactionHistoryInterface) {
        Call<TransactionHistoryResponse> call = postInterfaceService.getTransactionHistory(transactionHistorySendObject);
        call.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<TransactionHistoryResponse> call, @NonNull Response<TransactionHistoryResponse> response) {
                if (response.isSuccessful()) {
                    TransactionHistoryResponse transactionHistoryResponse = response.body();

                    if (transactionHistoryResponse != null) {
                        if (transactionHistoryResponse.getCode().equals(Constants.SUCCESS)) {
                            transactionHistoryInterface.onSuccess(transactionHistoryResponse);
                        } else {
                            transactionHistoryInterface.onError("Error. Try again later" + transactionHistoryResponse.getCode());
                        }
                    }
                } else {
                    transactionHistoryInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionHistoryResponse> call, @NonNull Throwable t) {
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        transactionHistoryInterface.onError("Network Error, please try again");
                    } else {
                        transactionHistoryInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    transactionHistoryInterface.onError("Network error");
                }
            }
        });
    }

    public void addSim(AddSimSendObject addSimSendObject, AddSimInterface addSimInterface) {
        Call<AddSimResponse> call = postInterfaceService.addSim(addSimSendObject);
        call.enqueue(new Callback<AddSimResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddSimResponse> call, @NonNull Response<AddSimResponse> response) {
                if (response.isSuccessful()) {
                    AddSimResponse addSimResponse = response.body();

                    if (addSimResponse != null) {
                        if (addSimResponse.getCode().equals(Constants.SUCCESS)) {
                            addSimInterface.onSuccess(addSimResponse);
                        } else {
                            addSimInterface.onError(addSimResponse.getDescription());
                        }
                    }
                } else {
                    addSimInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddSimResponse> call, @NonNull Throwable t) {
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        addSimInterface.onError("Network Error, please try again");
                    } else {
                        addSimInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    addSimInterface.onError("Network error");
                }
            }
        });
    }

    public void deleteAccount(DeleteAccountSendObject deleteAccountSendObject, DeleteAccountInterface deleteAccountInterface) {
        Call<DeleteAccountResponse> call = postInterfaceService.deleteAccount(deleteAccountSendObject);
        call.enqueue(new Callback<DeleteAccountResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeleteAccountResponse> call, @NonNull Response<DeleteAccountResponse> response) {
                if (response.isSuccessful()) {
                    DeleteAccountResponse deleteAccountResponse = response.body();

                    if (deleteAccountResponse != null) {
                        if (deleteAccountResponse.getCode().equals(Constants.SUCCESS)) {
                            deleteAccountInterface.onSuccess(deleteAccountResponse);
                        } else {
                            deleteAccountInterface.onError(deleteAccountResponse.getDescription());
                        }
                    }
                } else {
                    deleteAccountInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeleteAccountResponse> call, @NonNull Throwable t) {
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        deleteAccountInterface.onError("Network Error, please try again");
                    } else {
                        deleteAccountInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    deleteAccountInterface.onError("Network error");
                }
            }
        });
    }

    public void forgotPassword(ForgotPasswordSendObject forgotPasswordSendObject, ForgotPasswordInterface forgotPasswordInterface) {
        Call<ForgotPasswordResponse> call = postInterfaceService.forgotPassword(forgotPasswordSendObject);
        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForgotPasswordResponse> call, @NonNull Response<ForgotPasswordResponse> response) {
                if (response.isSuccessful()) {
                    ForgotPasswordResponse forgotPasswordResponse = response.body();

                    if (forgotPasswordResponse != null) {
                        if (forgotPasswordResponse.getCode().equals(Constants.SUCCESS)) {
                            forgotPasswordInterface.onSuccess(forgotPasswordResponse);
                        } else {
                            forgotPasswordInterface.onError(forgotPasswordResponse.getDescription());
                        }
                    }
                } else {
                    forgotPasswordInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgotPasswordResponse> call, @NonNull Throwable t) {
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        forgotPasswordInterface.onError("Network Error, please try again");
                    } else {
                        forgotPasswordInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    forgotPasswordInterface.onError("Network error");
                }
            }
        });
    }

    public void deleteSingleTransaction(DeleteTransactionSendObject deleteTransactionSendObject, DeleteSingleHistoryInterface deleteSingleHistoryInterface) {
        Call<DeleteTransactionResponse> call = postInterfaceService.deleteAsingleTransaction(deleteTransactionSendObject);
        call.enqueue(new Callback<DeleteTransactionResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeleteTransactionResponse> call, @NonNull Response<DeleteTransactionResponse> response) {
                if (response.isSuccessful()) {
                    DeleteTransactionResponse deleteAccountResponse = response.body();

                    if (deleteAccountResponse != null) {
                        if (deleteAccountResponse.getCode().equals(Constants.SUCCESS)) {
                            deleteSingleHistoryInterface.onSuccess(deleteAccountResponse);
                        } else {
                            deleteSingleHistoryInterface.onError(deleteAccountResponse.getDescription());
                        }
                    }
                } else {
                    deleteSingleHistoryInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeleteTransactionResponse> call, @NonNull Throwable t) {
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        deleteSingleHistoryInterface.onError("Network Error, please try again");
                    } else {
                        deleteSingleHistoryInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    deleteSingleHistoryInterface.onError("Network error");
                }
            }
        });
    }

    public void deleteAsim(DeleteSimSendObject deleteSimSendObject, DeleteSimInterface deleteSimInterface) {
        Call<DeleteSimResponse> call = postInterfaceService.deleteSim(deleteSimSendObject);
        call.enqueue(new Callback<DeleteSimResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeleteSimResponse> call, @NonNull Response<DeleteSimResponse> response) {
                if (response.isSuccessful()) {
                    DeleteSimResponse deleteAccountResponse = response.body();

                    if (deleteAccountResponse != null) {
                        if (deleteAccountResponse.getCode().equals(Constants.SUCCESS)) {
                            deleteSimInterface.onSuccess(deleteAccountResponse);
                        } else {
                            deleteSimInterface.onError(deleteAccountResponse.getDescription());
                        }
                    }
                } else {
                    deleteSimInterface.onError("Network error, please try again.");
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeleteSimResponse> call, @NonNull Throwable t) {
                if (t.getMessage() != null) {
                    if (Objects.requireNonNull(t.getMessage()).contains("failed to connect")) {
                        deleteSimInterface.onError("Network Error, please try again");
                    } else {
                        deleteSimInterface.onError(t.getMessage());
                    }
                    String error = (t.getMessage() == null) ? "No error message" : t.getMessage();
                    Log.e("Login error", error);
                } else {
                    deleteSimInterface.onError("Network error");
                }
            }
        });
    }
}
