package tingtel.payment.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sim3 {

    @SerializedName("phone3")
    @Expose
    private String phone3;
    @SerializedName("sim3_serial")
    @Expose
    private String sim3Serial;
    @SerializedName("sim3_user_network")
    @Expose
    private String sim3UserNetwork;

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getSim3Serial() {
        return sim3Serial;
    }

    public void setSim3Serial(String sim3Serial) {
        this.sim3Serial = sim3Serial;
    }

    public String getSim3UserNetwork() {
        return sim3UserNetwork;
    }

    public void setSim3UserNetwork(String sim3UserNetwork) {
        this.sim3UserNetwork = sim3UserNetwork;
    }
}
