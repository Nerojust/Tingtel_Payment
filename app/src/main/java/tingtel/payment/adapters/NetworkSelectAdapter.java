package tingtel.payment.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tingtel.payment.R;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.NetworkSelect;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

public class NetworkSelectAdapter extends RecyclerView.Adapter<NetworkSelectAdapter.MyViewHolder> {
    private final Context mContext;
    private final Activity activity;
    private final List<NetworkSelect> mData;
    private SessionManager sessionManager = AppUtils.getSessionManagerInstance();
    private AppDatabase appDatabase;
    private int selectedItem;


    public NetworkSelectAdapter(Context mContext, List lst, Activity activity) {

        this.mContext = mContext;
        this.mData = lst;
        this.activity = activity;
        appDatabase = AppDatabase.getDatabaseInstance(mContext);
        selectedItem = -1;

    }

    @NonNull
    @Override
    public NetworkSelectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.row_network, parent, false);
        // click listener here
        return new NetworkSelectAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final NetworkSelectAdapter.MyViewHolder holder, final int position) {
        holder.itemView.setTag(mData.get(position));
        holder.imgNetwork.setImageResource(mData.get(position).getImage());
        holder.tvNetworkName.setText(mData.get(position).getName());

        holder.imgCheck.setVisibility(View.GONE);

        if (selectedItem == position) {
            holder.imgCheck.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {

            int previousItem = selectedItem;
            selectedItem = position;

            notifyItemChanged(previousItem);
            notifyItemChanged(position);

            sessionManager.setSelectedRvNetwork(mData.get(position).getName());
        });


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgNetwork;
        final ImageView imgCheck;
        final TextView tvNetworkName;


        MyViewHolder(View itemView) {
            super(itemView);
            imgNetwork = itemView.findViewById(R.id.img_network);
            imgCheck = itemView.findViewById(R.id.img_check);
            tvNetworkName = itemView.findViewById(R.id.tv_name);
        }
    }
}
