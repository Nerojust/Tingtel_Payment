package tingtel.payment.models.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SendOTPresponse {

    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("msisdn")
    @Expose
    private String msisdn;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

}
