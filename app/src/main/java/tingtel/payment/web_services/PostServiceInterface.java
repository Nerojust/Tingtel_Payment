package tingtel.payment.web_services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
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
import tingtel.payment.models.validate_user.ValidateUserResponse;
import tingtel.payment.models.validate_user.ValidateUserSendObject;

public interface PostServiceInterface {

    @POST("register")
    Call<CustomerRegistrationResponse> createNewUser(@Body CustomerRegistrationSendObject customerRegistrationSendObject);

    @POST("login")
    Call<CustomerLoginResponse> loginUser(@Body CustomerLoginSendObject customerLoginSendObject);

    @POST("user_info")
    Call<CustomerInfoResponse> getCustomerInfo(@Body CustomerInfoSendObject customerInfoSendObject);

    @POST("changeemail")
    Call<ChangeEmailResponse> changeEmailAddress(@Body ChangeEmailSendObject changeEmailSendObject);

    @POST("changepassword")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordSendObject changePasswordSendObject);

    @POST("Report_Issue")
    Call<ReportIssueResponse> reportIssue(@Body ReportIssueSendObject reportIssueSendObject);

    @POST("credit_notification_push")
    Call<SendCreditDetailsResponse> sendCreditInfoToServer(@Body SendCreditDetailsSendObject transactionHistorySendObject);

    @POST("OTP")
    Call<SendOTPresponse> sendOTP(@Body SendOTPsendObject sendOTPsendObject);

    @POST("credit_update_report")
    Call<CheckTransactionStatusResponse> checkStatusOfTransaction(@Body CheckTransactionStatusSendObject checkTransactionStatusSendObject);

    @POST("get_transaction_history")
    Call<TransactionHistoryResponse> getTransactionHistory(@Body TransactionHistorySendObject transactionHistorySendObject);

    @POST("addsim")
    Call<AddSimResponse> addSim(@Body AddSimSendObject addSimSendObject);

    @POST("deleteAccount")
    Call<DeleteAccountResponse> deleteAccount(@Body DeleteAccountSendObject deleteAccountSendObject);

    @POST("delete_sim")
    Call<DeleteSimResponse> deleteSim(@Body DeleteSimSendObject deleteSimSendObject);

    @POST("delete_single_transaction")
    Call<DeleteTransactionResponse> deleteAsingleTransaction(@Body DeleteTransactionSendObject deleteTransactionSendObject);

    @POST("forgot_password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordSendObject forgotPasswordSendObject);

    @POST("validate_user")
    Call<ValidateUserResponse> validateUser(@Body ValidateUserSendObject validateUserSendObject);

}
