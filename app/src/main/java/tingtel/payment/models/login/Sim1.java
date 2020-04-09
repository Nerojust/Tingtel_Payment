package tingtel.payment.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sim1 {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("sim1_serial")
    @Expose
    private String sim1Serial;
    @SerializedName("user_network")
    @Expose
    private String userNetwork;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSim1Serial() {
        return sim1Serial;
    }

    public void setSim1Serial(String sim1Serial) {
        this.sim1Serial = sim1Serial;
    }

    public String getUserNetwork() {
        return userNetwork;
    }

    public void setUserNetwork(String userNetwork) {
        this.userNetwork = userNetwork;
    }

}
