package tingtel.payment.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tingtel.payment.R;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;

public class SimCardsAdapter extends RecyclerView.Adapter<SimCardsAdapter.MyViewHolder> {
    private final Context mContext;
    private final Activity activity;
    private final List<SimCards> mData;
    private AppDatabase appDatabase;
    private Fragment navhost;
    private NavController navController;
    SessionManager sessionManager = AppUtils.getSessionManagerInstance();

    public SimCardsAdapter(Context mContext, List lst, Activity activity) {

        this.mContext = mContext;
        this.mData = lst;
        this.activity = activity;
        appDatabase = AppDatabase.getDatabaseInstance(mContext);
    }

    @Override
    public SimCardsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.row_sim_card, parent, false);
        // click listener here
        return new SimCardsAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final SimCardsAdapter.MyViewHolder holder, final int position) {
        holder.itemView.setTag(mData.get(position));
        holder.imgDelete.setTag(mData.get(position));
        holder.tvPhoneNumber.setText(mData.get(position).getPhoneNumber());
        holder.tvNetworkName.setText(mData.get(position).getSimNetwork());


        if (mData.get(position).getPhoneNumber().equalsIgnoreCase(sessionManager.getSimPhoneNumber()) ||
                mData.get(position).getPhoneNumber().equalsIgnoreCase(sessionManager.getSimPhoneNumber1())) {
            holder.on_off_switch.setChecked(true);
        } else {
            holder.on_off_switch.setChecked(false);
        }

        if (holder.tvNetworkName.getText().toString().substring(0, 3).equalsIgnoreCase("mtn")) {
            holder.imageNetwork.setBackgroundResource(R.drawable.mtn_logo);
        } else if (holder.tvNetworkName.getText().toString().substring(0, 3).equalsIgnoreCase("air")) {
            holder.imageNetwork.setBackgroundResource(R.drawable.airtel_logo);
        } else if (holder.tvNetworkName.getText().toString().substring(0, 3).equalsIgnoreCase("glo")) {
            holder.imageNetwork.setBackgroundResource(R.drawable.glo_logo);
        } else if (holder.tvNetworkName.getText().toString().substring(0, 3).equalsIgnoreCase("9mo")
                || (holder.tvNetworkName.getText().toString().substring(0, 3).equalsIgnoreCase("eti"))) {
            holder.imageNetwork.setBackgroundResource(R.drawable.nmobile_logo);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tvPhoneNumber;
        final TextView tvNetworkName;
        final ImageView imgDelete;
        final Switch on_off_switch;
        private final ImageView imageNetwork;

        MyViewHolder(View itemView) {
            super(itemView);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            tvNetworkName = itemView.findViewById(R.id.tv_network_name);
            imgDelete = itemView.findViewById(R.id.btn_delete);
            imageNetwork = itemView.findViewById(R.id.img_network);
            on_off_switch = itemView.findViewById(R.id.switchbutton);
            on_off_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", "" + isChecked);
                    if (isChecked) {
                        on_off_switch.setText("Active");
                        // do something when check is selected
                    } else {
                        //do something when unchecked
                        on_off_switch.setText("Inactive");
                    }
                }

            });


            imgDelete.setOnClickListener(v -> {
//                navhost = ((SettingsActivity) activity).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//                navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                ViewGroup viewGroup = activity.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete_sim, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                Button btnYes = dialogView.findViewById(R.id.btn_yes);
                Button btnNo = dialogView.findViewById(R.id.btn_no);

                btnYes.setOnClickListener(v1 -> {

                    SimCards SimCardsModel = (SimCards) v.getTag();
                    int id = SimCardsModel.getId();
                    appDatabase.simCardsDao().deleteSimCard(id);
                    activity.startActivity(activity.getIntent());
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                    alertDialog.dismiss();

                });

                btnNo.setOnClickListener(v12 -> alertDialog.dismiss());
                alertDialog.show();
            });
        }
    }
}
