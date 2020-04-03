package tingtel.payment.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import tingtel.payment.R;
import tingtel.payment.activities.history.StatusActivity;
import tingtel.payment.models.transaction_history.TransactionHistoryResponse;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private final Context mContext;
    private final TransactionHistoryResponse transactionHistoryResponse;

    public HistoryAdapter(Context mContext, TransactionHistoryResponse transactionHistoryResponse) {
        this.mContext = mContext;
        this.transactionHistoryResponse = transactionHistoryResponse;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.row_history, parent, false);
        // click listener here
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //set animation for recycler view
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
        holder.tvAmount.setText(mContext.getResources().getString(R.string.naira) + transactionHistoryResponse.getTransactions().get(position).getAmount());
        //holder.tvDate.setText(new SimpleDateFormat("MMMM dd, hh:mm a").format(transactionHistoryResponse.getTransactions().get(position).getCreatedAt()));
        holder.tvSenderPhoneNumber.setText(transactionHistoryResponse.getTransactions().get(position).getUserPhone());
        holder.tvReceiverPhoneNumber.setText(transactionHistoryResponse.getTransactions().get(position).getBeneficiaryMsisdn());
        holder.ref_id.setText(transactionHistoryResponse.getTransactions().get(position).getRef());
        Integer statusId = transactionHistoryResponse.getTransactions().get(position).getStatus();
        if (statusId == 0) {
            holder.status.setText("Pending");
        } else if (statusId == 1) {
            holder.status.setText("Completed");
        }
        String senderNetwork = transactionHistoryResponse.getTransactions().get(position).getSourceNetwork();
        String receiverNetwork = transactionHistoryResponse.getTransactions().get(position).getBeneficiaryNetwork();

        setNetworkLogo(senderNetwork, holder.imageSenderNetwork);
        setNetworkLogo(receiverNetwork, holder.imgReceiverNetwork);


        holder.container.setOnClickListener(v ->{
            Intent intent = new Intent(mContext, StatusActivity.class);
            intent.putExtra("amount", transactionHistoryResponse.getTransactions().get(position).getAmount());
            intent.putExtra("ref_id", transactionHistoryResponse.getTransactions().get(position).getRef());
            intent.putExtra("status", transactionHistoryResponse.getTransactions().get(position).getStatus());
            intent.putExtra("sender_number", transactionHistoryResponse.getTransactions().get(position).getUserPhone());
            intent.putExtra("receiver_number",transactionHistoryResponse.getTransactions().get(position).getBeneficiaryMsisdn());
            mContext.startActivity(intent);
        });

    }

    private void setNetworkLogo(String networkLogo, ImageView imageView) {
        if (networkLogo.substring(0, 3).equalsIgnoreCase("mtn")) {
            imageView.setBackgroundResource(R.drawable.mtn_logo);
        } else if (networkLogo.substring(0, 3).equalsIgnoreCase("air")) {
            imageView.setBackgroundResource(R.drawable.airtel_logo);
        } else if (networkLogo.substring(0, 3).equalsIgnoreCase("glo")) {
            imageView.setBackgroundResource(R.drawable.glo_logo);
        } else if (networkLogo.substring(0, 3).equalsIgnoreCase("9mo")
                || (networkLogo.substring(0, 3).equalsIgnoreCase("eti"))) {
            imageView.setBackgroundResource(R.drawable.nmobile_logo);
        }
    }

    @Override
    public int getItemCount() {
        return transactionHistoryResponse.getTransactions().size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tvDate, status, ref_id;
        final TextView tvAmount;
        final TextView tvSenderPhoneNumber;
        final TextView tvReceiverPhoneNumber;
        final ImageView imgReceiverNetwork, imageSenderNetwork;
        final ImageView btnDelete;
        final LinearLayout container;

        MyViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.layoutContainer);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvSenderPhoneNumber = itemView.findViewById(R.id.tv_sender_phone_number);
            tvReceiverPhoneNumber = itemView.findViewById(R.id.tv_receiver_phone_number);
            imgReceiverNetwork = itemView.findViewById(R.id.img_receiver_network);
            imageSenderNetwork = itemView.findViewById(R.id.img_sender_network);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            status = itemView.findViewById(R.id.status);
            ref_id = itemView.findViewById(R.id.ref_id_tv);
        }
    }


}
