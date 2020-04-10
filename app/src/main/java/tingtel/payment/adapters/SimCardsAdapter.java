package tingtel.payment.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;
import tingtel.payment.models.delete_sim.DeleteSimResponse;
import tingtel.payment.models.delete_sim.DeleteSimSendObject;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;
import tingtel.payment.web_services.WebSeviceRequestMaker;
import tingtel.payment.web_services.interfaces.DeleteSimInterface;

public class SimCardsAdapter extends RecyclerView.Adapter<SimCardsAdapter.MyViewHolder> {
    private final Context mContext;
    private final Activity activity;
    private final List<SimCards> mData;
    SessionManager sessionManager = AppUtils.getSessionManagerInstance();
    private AppDatabase appDatabase;
    private Fragment navhost;
    private NavController navController;
    private int now_position;

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
        view = mInflater.inflate(R.layout.manage_sim_cards_layout, parent, false);
        // click listener here
        return new SimCardsAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final SimCardsAdapter.MyViewHolder holder, final int position) {
        holder.itemView.setTag(mData.get(position));
        holder.imgDelete.setTag(mData.get(position));
        Log.e("TingtelApp", "first number is" + AppUtils.checkPhoneNumberAndRemovePrefix(mData.get(position).getPhoneNumber()));
        Log.e("TingtelApp", "second number is" + mData.get(position).getPhoneNumber());

        holder.tvPhoneNumber.setText(AppUtils.checkPhoneNumberAndRemovePrefix(mData.get(position).getPhoneNumber()));
        holder.tvNetworkName.setText(mData.get(position).getSimNetwork());
        now_position = position;

        if (mData.get(position).getPhoneNumber().equalsIgnoreCase(sessionManager.getSimOnePhoneNumber()) ||
                mData.get(position).getPhoneNumber().equalsIgnoreCase(sessionManager.getSimTwoPhoneNumber())) {
            holder.on_off_switch.setChecked(true);

        } else {
            holder.on_off_switch.setChecked(false);
            holder.on_off_switch.setEnabled(false);
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
            on_off_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Log.v("Switch State=", "" + isChecked);
                if (isChecked) {
                    on_off_switch.setText("Active");
                    // do something when check is selected
                } else {
                    //do something when unchecked
                    on_off_switch.setText("Inactive");
                }
            });


            imgDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                ViewGroup viewGroup = activity.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete_sim, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                Button btnYes = dialogView.findViewById(R.id.btn_yes);
                Button btnNo = dialogView.findViewById(R.id.btn_no);

                btnYes.setOnClickListener(v1 -> {
                    AppUtils.initLoadingDialog(mContext);

                    SimCards SimCardsModel = (SimCards) v.getTag();
                    DeleteSimSendObject deleteSimSendObject = new DeleteSimSendObject();
                    deleteSimSendObject.setPhone_number(AppUtils.checkPhoneNumberAndRestructure(SimCardsModel.getPhoneNumber()));
                    Log.e("TingtelApp", "my request number is" + AppUtils.checkPhoneNumberAndRestructure(mData.get(now_position).getPhoneNumber()));
                    deleteSimSendObject.setPhone(sessionManager.getNumberFromLogin());
                    deleteSimSendObject.setHash(AppUtils.generateHash("tingtel", BuildConfig.HEADER_PASSWORD));

                    WebSeviceRequestMaker webSeviceRequestMaker = new WebSeviceRequestMaker();
                    webSeviceRequestMaker.deleteAsim(deleteSimSendObject, new DeleteSimInterface() {
                        @Override
                        public void onSuccess(DeleteSimResponse deleteSimResponse) {
                            AppUtils.showDialog("Deleted Successfully", activity);
                            int id = SimCardsModel.getId();
                            appDatabase.simCardsDao().deleteSimCard(id);
                            activity.startActivity(activity.getIntent());
                            activity.finish();
                            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            alertDialog.dismiss();
                            AppUtils.dismissLoadingDialog();
                        }

                        @Override
                        public void onError(String error) {
                            AppUtils.dismissLoadingDialog();
                            AppUtils.showDialog(error, activity);
                            Log.e("TingtelApp", "my error is" + error);
                        }

                        @Override
                        public void onErrorCode(int errorCode) {
                            AppUtils.dismissLoadingDialog();
                            AppUtils.showDialog(String.valueOf(errorCode), activity);
                        }
                    });
                });

                btnNo.setOnClickListener(v12 -> alertDialog.dismiss());
                alertDialog.show();
            });
        }
    }
}
