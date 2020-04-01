package tingtel.payment.web_services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tingtel.payment.models.change_Email.ChangeEmailResponse;
import tingtel.payment.models.change_Email.ChangeEmailSendObject;
import tingtel.payment.models.change_Password.ChangePasswordResponse;
import tingtel.payment.models.change_Password.ChangePasswordSendObject;
import tingtel.payment.models.customerInfo.CustomerInfoResponse;
import tingtel.payment.models.customerInfo.CustomerInfoSendObject;
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
import tingtel.payment.models.transaction_status.CheckTransactionStatusResponse;
import tingtel.payment.models.transaction_status.CheckTransactionStatusSendObject;

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

    @POST("credit_notification_push")
    Call<CheckTransactionStatusResponse> checkTransactionResult(@Body CheckTransactionStatusSendObject checkTransactionStatusSendObject);
}
