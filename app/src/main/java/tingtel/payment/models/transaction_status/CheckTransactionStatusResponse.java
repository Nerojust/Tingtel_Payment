package tingtel.payment.models.transaction_status;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckTransactionStatusResponse {

    @SerializedName("transactions")
    @Expose
    private Transactions transactions;

    public Transactions getTransactions() {
        return transactions;
    }

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

}

