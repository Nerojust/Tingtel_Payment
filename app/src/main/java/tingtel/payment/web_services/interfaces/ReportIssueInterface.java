package tingtel.payment.web_services.interfaces;


import tingtel.payment.models.Report_Issue.ReportIssueResponse;

public interface ReportIssueInterface {
    void onSuccess(ReportIssueResponse reportIssueResponse);

    void onError(String error);

    void onErrorCode(int errorCode);

}

