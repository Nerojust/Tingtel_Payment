package tingtel.payment.models.otp;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOTPsendObject {

    @SerializedName("network")
    @Expose
    private String network;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
