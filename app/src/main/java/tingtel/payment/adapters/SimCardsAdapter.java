package tingtel.payment.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import tingtel.payment.R;
import tingtel.payment.SettingsActivity;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;

public class SimCardsAdapter extends RecyclerView.Adapter<SimCardsAdapter.MyViewHolder> {
    private final Context mContext;
    private final Activity activity;
    private final List<SimCards> mData;
    AppDatabase appDatabase;
    Fragment navhost;
    NavController navController;

    public SimCardsAdapter(Context mContext, List lst, Activity activity) {

        this.mContext = mContext;
        this.mData = lst;
        this.activity = activity;
        appDatabase = AppDatabase.getDatabaseInstance(mContext);
    }

    @Override
    public SimCardsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.row_sim_card, parent, false);
        // click listener here
        return new SimCardsAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final SimCardsAdapter.MyViewHolder holder, final int position) {
        holder.itemView.setTag(mData.get(position));
        holder.btnDelete.setTag(mData.get(position));
        holder.tvPhoneNumber.setText(mData.get(position).getPhoneNumber());
        holder.tvNetworkName.setText(mData.get(position).getSimNetwork());



    }




    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tvPhoneNumber;
        final TextView tvNetworkName;
        final Button btnDelete;



        MyViewHolder(View itemView) {
            super(itemView);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            tvNetworkName = itemView.findViewById(R.id.tv_network_name);
            btnDelete = itemView.findViewById(R.id.btn_delete);




            Context context = itemView.getContext();

            itemView.setOnClickListener(view -> {
                SimCards SimCardsModel = (SimCards) view.getTag();

//                    Intent i = new Intent(view.getContext(), MainActivity.class);
//                    i.putExtra("desc", cpu.getCode());
//                    i.putExtra("title", cpu.getName());
//                    view.getContext().startActivity(i);
                Toast.makeText(mContext, "f" + SimCardsModel.getPhoneNumber(), Toast.LENGTH_LONG).show();

                // method.DialUssdCode((BanksSimCardssActivity)context, SimCardsModel.get, context, 0);
            });
            itemView.setOnLongClickListener(v -> true);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navhost = ((SettingsActivity) activity).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    navController = NavHostFragment.findNavController(navhost);

                    SimCards SimCardsModel = (SimCards) v.getTag();

                    int id = SimCardsModel.getId();

                    appDatabase.simCardsDao().deleteSimCard(id);

                    navController.navigate(R.id.action_manageSimsFragment_self, null);


                }
            });
        }

    }


}
