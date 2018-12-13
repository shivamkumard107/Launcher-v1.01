package launcher.astanite.com.astanite.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import launcher.astanite.com.astanite.*;
import launcher.astanite.com.astanite.data.AppInfo;
import launcher.astanite.com.astanite.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private static final String TAG = AppsAdapter.class.getSimpleName();

    private List<AppInfo> appsList;
    private RequestManager glide;
    private Context context;

    public AppsAdapter(List<AppInfo> appsList, RequestManager glide, Context context) {
        this.appsList = appsList;
        this.glide = glide;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.app_drawer_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindValues(appsList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView appIconImageview;
        private TextView appNameTextview;
        private View appItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appIconImageview = itemView.findViewById(R.id.appIconImageview);
            appNameTextview = itemView.findViewById(R.id.appNameTextview);
            appItem = itemView;
        }

        public void bindValues(AppInfo app, Context context) {
            glide.load(app.icon).into(appIconImageview);
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            appIconImageview.setColorFilter(filter);
            appNameTextview.setText(app.label);
            appItem.setOnClickListener(view -> context.startActivity(app.launchIntent));
            appItem.setOnLongClickListener(view -> {
                SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
                editor.putString("packageName",app.packageName);
                editor.apply();
                ((Activity)context).registerForContextMenu(appItem);
                return false;
            });
        }
    }

    public void updateAppsList(List<AppInfo> newList) {
        this.appsList = newList;
        notifyDataSetChanged();
    }


}
