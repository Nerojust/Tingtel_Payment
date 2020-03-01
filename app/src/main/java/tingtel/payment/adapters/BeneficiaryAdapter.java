package tingtel.payment.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import tingtel.payment.R;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.Beneficiary;

public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.MyViewHolder> {

    private final Context mContext;
    private final Activity activity;
    private final List<Beneficiary> mData;
    private AppDatabase appDatabase;
    private Fragment navhost;
    private NavController navController;

    public BeneficiaryAdapter(Context mContext, List lst, Activity activity) {

        this.mContext = mContext;
        this.mData = lst;
        this.activity = activity;
        appDatabase = AppDatabase.getDatabaseInstance(mContext);
    }

    @Override
    public BeneficiaryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.row_beneficiary, parent, false);
        // click listener here
        return new BeneficiaryAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final BeneficiaryAdapter.MyViewHolder holder, final int position) {
        holder.itemView.setTag(mData.get(position));
        holder.imgDelete.setTag(mData.get(position));
        holder.tvPhoneNumber.setText(mData.get(position).getPhoneNumber());
        holder.tvNetworkName.setText(mData.get(position).getNetwork());
        holder.tvName.setText(mData.get(position).getName());

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
        final TextView tvName;
        final ImageView imgDelete;
        private final ImageView imageNetwork;

        MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            tvNetworkName = itemView.findViewById(R.id.tv_network_name);
            imgDelete = itemView.findViewById(R.id.btn_delete);
            imageNetwork = itemView.findViewById(R.id.img_network);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity, "kjjkk", Toast.LENGTH_LONG).show();
                    Beneficiary BeneficiaryModel = (Beneficiary) v.getTag();
                    Intent intent = new Intent("selectedbeneficiary");
                    intent.putExtra("phoneNumber", BeneficiaryModel.getPhoneNumber());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                }
            });


            imgDelete.setOnClickListener(v -> {
//                navhost = ((SettingsActivity) activity).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//                navController = NavHostFragment.findNavController(Objects.requireNonNull(navhost));

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                ViewGroup viewGroup = activity.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete_beneficiary, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                Button btnYes = dialogView.findViewById(R.id.btn_yes);
                Button btnNo = dialogView.findViewById(R.id.btn_no);

                btnYes.setOnClickListener(v1 -> {

                    Beneficiary BeneficiaryModel = (Beneficiary) v.getTag();
                    int id = BeneficiaryModel.getId();
                    appDatabase.beneficiaryDao().deleteBeneficiary(id);
                    alertDialog.dismiss();

                });

                btnNo.setOnClickListener(v12 -> alertDialog.dismiss());
                alertDialog.show();
            });
        }
    }

}
