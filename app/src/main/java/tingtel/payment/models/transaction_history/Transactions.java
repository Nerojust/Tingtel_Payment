package tingtel.payment.models.transaction_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transactions {

    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("source_network")
    @Expose
    private String sourceNetwork;
    @SerializedName("beneficiary_network")
    @Expose
    private String beneficiaryNetwork;
    @SerializedName("beneficiary_msisdn")
    @Expose
    private String beneficiaryMsisdn;
    @SerializedName("ref")
    @Expose
    private String ref;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSourceNetwork() {
        return sourceNetwork;
    }

    public void setSourceNetwork(String sourceNetwork) {
        this.sourceNetwork = sourceNetwork;
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
