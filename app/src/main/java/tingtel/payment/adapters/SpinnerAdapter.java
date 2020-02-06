package tingtel.payment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tingtel.payment.R;

public class SpinnerAdapter extends ArrayAdapter<String> {

    String[] spinnerTitles;
    int[] spinnerImages;

    Context mContext;

    public SpinnerAdapter(@NonNull Context context, String[] titles, int[] images, String[] population) {
        super(context, R.layout.row_spinner);
        this.spinnerTitles = titles;
        this.spinnerImages = images;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return spinnerTitles.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.row_spinner, parent, false);
            mViewHolder.mFlag = (ImageView) convertView.findViewById(R.id.img_spinner_image);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.tv_spinner_text);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageResource(spinnerImages[position]);
        mViewHolder.mName.setText(spinnerTitles[position]);

        return convertView;
    }

    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
    }
}