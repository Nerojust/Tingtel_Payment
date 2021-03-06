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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tingtel.payment.R;
import tingtel.payment.activities.history.StatusActivity;
import tingtel.payment.models.transaction_history.TransactionHistory;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

public class SimTwoHistoryAdapter extends RecyclerView.Adapter<SimTwoHistoryAdapter.MyViewHolder> {
    private final Context mContext;
    private final List<TransactionHistory> responseFromTransaction;
    private SessionManager sessionManager = AppUtils.getSessionManagerInstance();
    private int count;
    private int where;

    public SimTwoHistoryAdapter(Context mContext, List<TransactionHistory> responseFromTransaction) {
        this.mContext = mContext;
        this.responseFromTransaction = responseFromTransaction;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.row_history_sim_two, parent, false);
        // click listener here
        return new SimTwoHistoryAdapter.MyViewHolder(view);

    }

    private String formateDate(String dateString) {
        Date date;
        String formattedDate = "";
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateString);
            formattedDate = new SimpleDateFormat("MMMM dd, hh:mm a", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return formattedDate;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String amount = responseFromTransaction.get(position).getAmount();
        String date = responseFromTransaction.get(position).getCreatedAt();
        String sender_number = responseFromTransaction.get(position).getUserPhone();
        String receiver_number = responseFromTransaction.get(position).getBeneficiaryMsisdn();
        String reference_id = responseFromTransaction.get(position).getRef();
        int statusId = responseFromTransaction.get(position).getStatus();
        String receiver_network = responseFromTransaction.get(position).getBeneficiaryNetwork();
        String sender_network = responseFromTransaction.get(position).getSourceNetwork();

        //set animation for recycler view
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        holder.tvAmount.setText(String.format("%s%s", mContext.getResources().getString(R.string.naira), amount));
        holder.tvDate.setText(formateDate(date));
        holder.tvSenderPhoneNumber.setText(AppUtils.checkPhoneNumberAndRemovePrefix(sender_number));
        holder.tvReceiverPhoneNumber.setText(AppUtils.checkPhoneNumberAndRemovePrefix(receiver_number));
        holder.ref_id.setText(reference_id);

        if (statusId == 0) {
            holder.status.setText(mContext.getResources().getString(R.string.request_sent));
            holder.status.setTextColor(mContext.getResources().getColor(R.color.tingtel_red_color));
        } else if (statusId == 1) {
            holder.status.setText(mContext.getResources().getString(R.string.pending));
            holder.status.setTextColor(mContext.getResources().getColor(R.color.selected_dot));
        } else if (statusId == 2) {
            holder.status.setText(mContext.getResources().getString(R.string.completed));
            holder.status.setTextColor(mContext.getResources().getColor(R.color.green));
        }

        setNetworkLogo(sender_network, holder.imageSenderNetwork);
        setNetworkLogo(receiver_network, holder.imgReceiverNetwork);


        holder.container.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, StatusActivity.class);
            intent.putExtra("amount", amount);
            intent.putExtra("ref_id", reference_id);
            intent.putExtra("status", statusId);
            intent.putExtra("sender_number", sender_number);
            intent.putExtra("receiver_number", receiver_number);
            intent.putExtra("date", formateDate(date));
            mContext.startActivity(intent);
        });

    }

    private void setNetworkLogo(String networkLogo, ImageView imageView) {
        if (networkLogo.substring(0, 3).equalsIgnoreCase("mtn")) {
            imageView.setBackgroundResource(R.drawable.mtn_logo);
        } else if (networkLogo.substring(0, 3).equalsIgnoreCase("air")) {
            imageView.setBackgroundResource(R.drawable.airtellogo);
        } else if (networkLogo.substring(0, 3).equalsIgnoreCase("glo")) {
            imageView.setBackgroundResource(R.drawable.glo_logo);
        } else if (networkLogo.substring(0, 3).equalsIgnoreCase("9mo")
                || (networkLogo.substring(0, 3).equalsIgnoreCase("eti"))) {
            imageView.setBackgroundResource(R.drawable.nmobile_logo);
        }
    }

    @Override
    public int getItemCount() {
        return responseFromTransaction.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tvDate, status, ref_id;
        final TextView tvAmount;
        final TextView tvSenderPhoneNumber;
        final TextView tvReceiverPhoneNumber;
        final ImageView imgReceiverNetwork, imageSenderNetwork;
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
            status = itemView.findViewById(R.id.status);
            ref_id = itemView.findViewById(R.id.ref_id_tv);
        }
    }
}
