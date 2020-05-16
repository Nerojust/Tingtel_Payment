package tingtel.payment.models.credit_notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditNotificationResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

}

