package tingtel.payment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import tingtel.payment.R;
import tingtel.payment.models.History;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private final Context mContext;
    private final List<History> mData;

    public HistoryAdapter(Context mContext, List lst) {

        this.mContext = mContext;
        this.mData = lst;
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
        holder.itemView.setTag(mData.get(position));

        holder.tvAmount.setText("#" + mData.get(position).getAmount());
        holder.tvDate.setText(new SimpleDateFormat("MMMM dd, hh:mm a").format(mData.get(position).getDate()));
        holder.tvSenderPhoneNumber.setText(mData.get(position).getSenderPhoneNumber());
        holder.tvReceiverPhoneNumber.setText(mData.get(position).getReceiverPhoneNumber());


//        setNetworkImage(holder, mData.get(position).getSimName());
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tvDate;
        final TextView tvAmount;
        final TextView tvSenderPhoneNumber;
        final ImageView imgSenderNetwork;
        final TextView tvReceiverPhoneNumber;
        final ImageView imgReceiverNetwork;
        final Button btnDelete;


        MyViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvSenderPhoneNumber = itemView.findViewById(R.id.tv_sender_phone_number);
            imgSenderNetwork = itemView.findViewById(R.id.img_sender_network);
            tvReceiverPhoneNumber = itemView.findViewById(R.id.tv_receiver_phone_number);
            imgReceiverNetwork = itemView.findViewById(R.id.img_receiver_network);
            btnDelete = itemView.findViewById(R.id.btn_delete);


            Context context = itemView.getContext();

            itemView.setOnClickListener(view -> {
                History HistoryModel = (History) view.getTag();
//                    Intent i = new Intent(view.getContext(), MainActivity.class);
//                    i.putExtra("desc", cpu.getCode());
//                    i.putExtra("title", cpu.getName());
//                    view.getContext().startActivity(i);

                // method.DialUssdCode((BanksHistorysActivity)context, HistoryModel.get, context, 0);
            });
            itemView.setOnLongClickListener(v -> true);
        }
    }


}
