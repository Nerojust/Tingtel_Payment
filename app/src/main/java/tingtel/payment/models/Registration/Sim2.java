package tingtel.payment.models.Registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sim2 {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("serial_number")
    @Expose
    private String serialNumber;
    @SerializedName("user_network")
    @Expose
    private String userNetwork;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUserNetwork() {
        return userNetwork;
    }

    public void setUserNetwork(String userNetwork) {
        this.userNetwork = userNetwork;
    }

}
