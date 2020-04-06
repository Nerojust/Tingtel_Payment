package tingtel.payment.models.transaction_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Phone1Transaction {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("source_network")
    @Expose
    private String sourceNetwork;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("ref")
    @Expose
    private String ref;
    @SerializedName("beneficiary_network")
    @Expose
    private String beneficiaryNetwork;
    @SerializedName("beneficiary_msisdn")
    @Expose
    private String beneficiaryMsisdn;
    @SerializedName("ussd_response_message")
    @Expose
    private Object ussdResponseMessage;
    @SerializedName("api_ref")
    @Expose
    private Object apiRef;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSourceNetwork() {
        return sourceNetwork;
    }

    public void setSourceNetwork(String sourceNetwork) {
        this.sourceNetwork = sourceNetwork;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getBeneficiaryNetwork() {
        return beneficiaryNetwork;
    }

    public void setBeneficiaryNetwork(String beneficiaryNetwork) {
        this.beneficiaryNetwork = beneficiaryNetwork;
    }

    public String getBeneficiaryMsisdn() {
        return beneficiaryMsisdn;
    }

    public void setBeneficiaryMsisdn(String beneficiaryMsisdn) {
        this.beneficiaryMsisdn = beneficiaryMsisdn;
    }

    public Object getUssdResponseMessage() {
        return ussdResponseMessage;
    }

    public void setUssdResponseMessage(Object ussdResponseMessage) {
        this.ussdResponseMessage = ussdResponseMessage;
    }

    public Object getApiRef() {
        return apiRef;
    }

    public void setApiRef(Object apiRef) {
        this.apiRef = apiRef;
    }

}
