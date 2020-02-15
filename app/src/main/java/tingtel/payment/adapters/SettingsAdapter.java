package tingtel.payment.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tingtel.payment.BuildConfig;
import tingtel.payment.R;
import tingtel.payment.SettingsActivity;
import tingtel.payment.SignInActivity;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {
    private final Context mContext;
    private final Activity activity;
    private final List<String> mData;
    Fragment navhost;
    NavController navController;

    public SettingsAdapter(Context mContext, List lst, Activity activity) {

        this.mContext = mContext;
        this.mData = lst;
        this.activity = activity;
    }

    @Override
    public SettingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.row_settings, parent, false);
        // click listener here
        return new SettingsAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final SettingsAdapter.MyViewHolder holder, final int position) {
        holder.itemView.setTag(mData.get(position));

        holder.tvTitle.setText(mData.get(position));


    }




    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTitle;



        MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_Title);
            Context context = itemView.getContext();

            itemView.setOnClickListener(view -> {
                String ClickedItem = (String) view.getTag();

                navhost = ((SettingsActivity) activity).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                navController = NavHostFragment.findNavController(navhost);

                if (ClickedItem.equalsIgnoreCase("Change Password")) {

                    navController.navigate(R.id.action_settingsHome_to_changePasswordFragment, null);


                } else if ((ClickedItem.equalsIgnoreCase("Change Email Address"))) {

                    navController.navigate(R.id.action_settingsHome_to_changeEmailAddress, null);


                } else if ((ClickedItem.equalsIgnoreCase("Add New Sim"))) {

                    navController.navigate(R.id.action_settingsHome_to_addNewSimFragment, null);


                } else if ((ClickedItem.equalsIgnoreCase("Manage Sims"))) {

                    navController.navigate(R.id.action_settingsHome_to_manageSimsFragment, null);


                } else if ((ClickedItem.equalsIgnoreCase("Tutorial (How to Use)"))) {

                    navController.navigate(R.id.action_settingsHome_to_tutorialFragment, null);


                }  else if ((ClickedItem.equalsIgnoreCase("Report An Issue"))) {

                    navController.navigate(R.id.action_settingsHome_to_reportIssueFragment, null);


                }  else if ((ClickedItem.equalsIgnoreCase("Privacy Policy"))) {

                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tingtel.com/privacy-policy"));
                        mContext.startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(mContext, "No Web browser Found.",  Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }  else if ((ClickedItem.equalsIgnoreCase("Delete Account"))) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    ViewGroup viewGroup = activity.findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_account, viewGroup, false);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();

                    Button btnYes = (Button)dialogView.findViewById(R.id.btn_yes);
                    Button btnNo = (Button)dialogView.findViewById(R.id.btn_no);

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(activity, SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(intent);

                        }
                    });

                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();


                }  else if ((ClickedItem.equalsIgnoreCase("Share App"))) {

                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                        String shareMessage= "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch(Exception e) {
                        //e.toString();
                    }

                }

            });



            itemView.setOnLongClickListener(v -> true);
        }
    }





}
