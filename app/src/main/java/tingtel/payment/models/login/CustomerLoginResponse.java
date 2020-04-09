package tingtel.payment.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CustomerLoginResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("user_info")
    @Expose
    private List<UserInfo> userInfo = null;
    @SerializedName("sim1")
    @Expose
    private List<Sim1> sim1 = null;
    @SerializedName("sim2")
    @Expose
    private List<Sim2> sim2 = null;
    @SerializedName("sim3")
    @Expose
    private List<Sim3> sim3 = null;
    @SerializedName("sim4")
    @Expose
    private List<Sim4> sim4 = null;

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

    public List<UserInfo> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(List<UserInfo> userInfo) {
        this.userInfo = userInfo;
    }

    public List<Sim1> getSim1() {
        return sim1;
    }

    public void setSim1(List<Sim1> sim1) {
        this.sim1 = sim1;
    }

    public List<Sim2> getSim2() {
        return sim2;
    }

    public void setSim2(List<Sim2> sim2) {
        this.sim2 = sim2;
    }

    public List<Sim3> getSim3() {
        return sim3;
    }

    public void setSim3(List<Sim3> sim3) {
        this.sim3 = sim3;
    }

    public List<Sim4> getSim4() {
        return sim4;
    }

    public void setSim4(List<Sim4> sim4) {
        this.sim4 = sim4;
    }

}


