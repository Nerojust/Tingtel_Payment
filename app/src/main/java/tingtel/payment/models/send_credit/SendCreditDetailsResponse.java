package tingtel.payment.models.send_credit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendCreditDetailsResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("transactions")
    @Expose
    private Transactions transactions;

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

    public Transactions getTransactions() {
        return transactions;
    }

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

}


