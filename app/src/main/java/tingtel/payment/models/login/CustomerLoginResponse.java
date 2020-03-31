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

}

