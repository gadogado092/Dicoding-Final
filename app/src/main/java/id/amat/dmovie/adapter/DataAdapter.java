package id.amat.dmovie.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import id.amat.dmovie.R;
import id.amat.dmovie.model.DataItem;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MovieViewHolder> {

    private ArrayList<DataItem> items;
    private Context context;

    public DataAdapter(Context context, ArrayList<DataItem> items) {
        this.items = items;
        this.context = context;
    }

    public void refill(ArrayList<DataItem> items) {
        this.items = new ArrayList<>();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new MovieViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvAdult;
        TextView tvDateRelease;
        TextView tvOverview;
        ImageView imgPoster;
        ProgressBar progressBar;

        MovieViewHolder(@NonNull View itemView) {

            super(itemView);
            tvTitle = itemView.findViewById(R.id.txt_title);
            tvDateRelease = itemView.findViewById(R.id.txt_date_release);
            tvOverview = itemView.findViewById(R.id.txt_overview);
            tvAdult = itemView.findViewById(R.id.txt_adult);
            imgPoster = itemView.findViewById(R.id.img_poster);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        void onBind(DataItem movieItem) {
            String title = checkTextIfNull(movieItem.getDataTitle());
            if (title.length() > 30) {
                tvTitle.setText(String.format("%s...", title.substring(0, 29)));
            } else {
                tvTitle.setText(checkTextIfNull(movieItem.getDataTitle()));
            }

            tvDateRelease.setText(movieItem.getDataReleaseDate());
            tvOverview.setText(movieItem.getDataOverview());
            if (!movieItem.getDataAdult()){
                tvAdult.setVisibility(View.INVISIBLE);
            }

            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w92"+movieItem.getDataPosterPath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(imgPoster);

        }

        String checkTextIfNull(String text) {
            if (text != null && !text.isEmpty()) {
                return text;
            } else {
                return "-";
            }
        }
    }
}
