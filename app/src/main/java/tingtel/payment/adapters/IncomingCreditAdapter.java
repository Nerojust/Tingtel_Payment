package tingtel.payment.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tingtel.payment.R;
import tingtel.payment.activities.history.StatusActivity;
import tingtel.payment.models.credit_notification.Data;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

public class IncomingCreditAdapter extends RecyclerView.Adapter<IncomingCreditAdapter.MyViewHolder> {
    private final Context mContext;
    private final List<Data> creditNotificationResponses;
    private int count;
    private int where;

    private SessionManager sessionManager = AppUtils.getSessionManagerInstance();

    public IncomingCreditAdapter(Context mContext, List<Data> creditNotificationResponses) {
        this.mContext = mContext;
        this.creditNotificationResponses = creditNotificationResponses;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.credit_items, parent, false);
        // click listener here
        return new IncomingCreditAdapter.MyViewHolder(view);

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
        String amount = creditNotificationResponses.get(position).getAmount();
        String date = creditNotificationResponses.get(position).getCreatedAt();
        String sender_number = creditNotificationResponses.get(position).getSender();
        String receiver_number = creditNotificationResponses.get(position).getPhone();
        String reference_id = creditNotificationResponses.get(position).getTransId();
        int statusId = creditNotificationResponses.get(position).getSent();
        String username = creditNotificationResponses.get(position).getName();

        //set animation for recycler view
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        holder.tvAmount.setText(String.format("%s%s", mContext.getResources().getString(R.string.naira), amount));
        holder.tvDate.setText(formateDate(date));
        holder.tvSenderPhoneNumber.setText(AppUtils.checkPhoneNumberAndRemovePrefix(sender_number));
        holder.tvReceiverPhoneNumber.setText(AppUtils.checkPhoneNumberAndRemovePrefix(receiver_number));

        holder.container.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, StatusActivity.class);
            intent.putExtra("amount", amount);
            intent.putExtra("ref_id", reference_id);
            intent.putExtra("status", statusId);
            intent.putExtra("sender_number", sender_number);
            intent.putExtra("receiver_number", receiver_number);
            intent.putExtra("date", formateDate(date));
            intent.putExtra("username", username);
            mContext.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return creditNotificationResponses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tvDate;
        final TextView tvAmount;
        final TextView tvSenderPhoneNumber;
        final TextView tvReceiverPhoneNumber;
        //  final ImageView btnDelete;
        final LinearLayout container;

        MyViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.layoutContainer);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvSenderPhoneNumber = itemView.findViewById(R.id.tv_sender_phone_number);
            tvReceiverPhoneNumber = itemView.findViewById(R.id.tv_receiver_phone_number);
        }
    }
}
