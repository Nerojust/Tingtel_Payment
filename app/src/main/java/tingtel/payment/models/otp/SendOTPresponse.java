package tingtel.payment.models.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOTPresponse {

    @SerializedName("msisdn")
    @Expose
    private String msisdn;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("description")
    @Expose
    private String description;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
